package com.example.wanderwise.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.preferences.UserPreferences
import com.example.wanderwise.R
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.data.local.database.CityFavoriteDao
import com.example.wanderwise.data.local.database.CityFavoriteDatabase
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.data.response.BodyUpload
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.data.response.LoginResponse
import com.example.wanderwise.data.response.RegisterResponse
import com.example.wanderwise.data.response.UploadImageResponse
import com.example.wanderwise.data.response.UploadPhotoResponse
import com.example.wanderwise.data.retrofit.ApiConfig
import com.example.wanderwise.data.retrofit.ApiService
import com.example.wanderwise.utils.Event
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PostRepository private constructor
(
    private val context: Context,
    private val apiService: ApiService,
    private val preferences: UserPreferences,
    private val databaseFav: CityFavoriteDao
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText


    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    val user = runBlocking { preferences.getSession().first() }
    val userUid = user.uid

    fun getAllFav(): LiveData<List<CityFavorite>> = databaseFav.getCity()

    fun getClickedCity(city: String): LiveData<CityFavorite> = databaseFav.getCityClicked(city)

    fun insertCityFav(city: CityFavorite) {
        executorService.execute { databaseFav.insertFav(city) }
    }

    fun deleteCityFav(city: CityFavorite) {
        executorService.execute { databaseFav.deleteFav(city) }
    }

    fun uploadImage(title: String, caption: String, image: File) = liveData {
        emit(Result.Loading)

        try {
            val titleRequestBody = title.toRequestBody("plain/text".toMediaType())
            val captionRequestBody = caption.toRequestBody("plain/text".toMediaType())
            val requestImageFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                image.name,
                requestImageFile
            )

            val user = runBlocking { preferences.getSession().first() }
            val successRespone = ApiConfig.getApiService(user.token).uploadImage(titleRequestBody, captionRequestBody, multipartBody)
            emit(Result.Success(successRespone.msg))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadImageResponse::class.java)
            emit(Result.Error("Sorry, ${errorResponse.msg}"))
        } catch (e: Exception) {
            emit(Result.Error(getStringError(context)))
        }
    }

    fun uploadPhotoUser(image: File) = liveData {
        emit(Result.Loading)

        try {
            val requestImageFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                image.name,
                requestImageFile
            )

            val user = runBlocking { preferences.getSession().first() }
            val successRespone = ApiConfig.getApiService(user.token).uploadImage(multipartBody)
            emit(Result.Success(successRespone.msg))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadPhotoResponse::class.java)
            emit(Result.Error("Sorry, ${errorResponse.msg}"))
        } catch (e: Exception) {
            emit(Result.Error(getStringError(context)))
        }
    }

    fun getImage(): LiveData<UploadPhotoResponse> {
        _isLoading.value = true
        val detail = MutableLiveData<UploadPhotoResponse>()

        val user = runBlocking { preferences.getSession().first() }
        val uploadImageResponse = ApiConfig.getApiService(user.token).getPhotoProfile()

        uploadImageResponse.enqueue(object : Callback<UploadPhotoResponse> {
            override fun onResponse(
                call: Call<UploadPhotoResponse>,
                response: Response<UploadPhotoResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    detail.value = response.body()
                } else {
                    _isLoading.value = false
                    _snackBarText.value = Event(getStringError(context))
                }
            }

            override fun onFailure(call: Call<UploadPhotoResponse>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = Event("${t.message}")
            }
        })

        return detail
    }

    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.register(name, email, password)
            emit(Result.Success("Thanks for register ${successResponse.body.username}"))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error("Sorry, ${errorResponse.message}"))
        } catch (e: Exception) {
            emit(Result.Error(getStringError(context)))
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val successResponse = apiService.login(email, password)
            val name = successResponse.body.name
            val token = successResponse.body.token
            val emailUser = successResponse.body.email
            val uidUser = successResponse.body.uid
            val isLogin = true
            Log.d("tokenCheck", token)
            saveSession(UserModel(name, token, emailUser, uidUser, "Location", "profile", isLogin))
            emit(Result.Success("Thanks ${successResponse.body.name} for the Login!"))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error("Sorry, ${errorResponse.msg}, please repeat!"))
        }
    }

    suspend fun saveSession(userModel: UserModel){
        preferences.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> {
        return preferences.getSession()
    }

    suspend fun logout(){
        preferences.logout()
    }

    private fun getStringError(context: Context): String {
        return context.getString(R.string.error_message_connection)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: PostRepository? = null
        fun getInstance(context: Context, apiService: ApiService, preferences: UserPreferences, databaseFav: CityFavoriteDao) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(context, apiService, preferences, databaseFav)
            }.also { instance = it }
    }
}