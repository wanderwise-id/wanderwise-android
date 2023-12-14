package com.example.wanderwise.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.preferences.UserPreferences
import com.example.wanderwise.R
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.data.response.LoginResponse
import com.example.wanderwise.data.response.RegisterResponse
import com.example.wanderwise.data.response.TestingUploadResponse
import com.example.wanderwise.data.response.UploadPostData
import com.example.wanderwise.data.retrofit.ApiConfig
import com.example.wanderwise.data.retrofit.ApiConfigTest
import com.example.wanderwise.data.retrofit.ApiService
import com.example.wanderwise.utils.Event
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class PostRepository private constructor
(
    private val context: Context,
    private val apiService: ApiService,
    private val preferences: UserPreferences
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun uploadImage(image: File): LiveData<TestingUploadResponse> {
        _isLoading.value = true
        val detail = MutableLiveData<TestingUploadResponse>()
        val requestImageFile = image.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            image.name,
            requestImageFile
        )

        val uploadImageResponse = ApiConfigTest.getApiService().testUpload(multipartBody)

        uploadImageResponse.enqueue(object : Callback<TestingUploadResponse> {
            override fun onResponse(
                call: Call<TestingUploadResponse>,
                response: Response<TestingUploadResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    detail.value = response.body()
                    _snackBarText.value = Event(response.message())
                } else {
                    _isLoading.value = false
                    _snackBarText.value = Event(getStringError(context))
                }
            }

            override fun onFailure(call: Call<TestingUploadResponse>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = Event("${t.message}")
            }
        })

        return detail
    }

    fun uploadImage(imageFile: File, name: String, price: Int): LiveData<UploadPostData> {
        _isLoading.value = true
        val detail = MutableLiveData<UploadPostData>()
        val requestBody = name.toRequestBody("text/plain".toMediaType())
        val requestBody2 = price.toString().toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        val user = runBlocking { preferences.getSession().first() }
        val uploadImageResponse = ApiConfig.getApiService(user.token).uploadPost(multipartBody, requestBody, requestBody2)

        uploadImageResponse.enqueue(object : Callback<UploadPostData> {
            override fun onResponse(
                call: Call<UploadPostData>,
                response: Response<UploadPostData>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    detail.value = response.body()
                    Log.d("testingResponse", response.message())
                    _snackBarText.value = Event(response.message())
                } else {
                    _isLoading.value = false
                    Log.d("testingResponse", response.message())
                    _snackBarText.value = Event(getStringError(context))
                }
            }

            override fun onFailure(call: Call<UploadPostData>, t: Throwable) {
                _isLoading.value = false
                Log.d("testingResponse", "${t.message}")
                _snackBarText.value = Event("${t.message}")
            }
        })

        return detail
    }

    fun registerUser(name: String, email: String, password: String): LiveData<RegisterResponse> {
        _isLoading.value = true
        val register = MutableLiveData<RegisterResponse>()
        val requestBody1 = name.toRequestBody("text/plain".toMediaType())
        val requestBody2 = email.toRequestBody("text/plain".toMediaType())
        val requestBody3 = password.toRequestBody("text/plain".toMediaType())

        Log.d("TestingUser", "$name, $email, $password")
        val registerUser = apiService.register(requestBody1, requestBody2, requestBody3)

        registerUser.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    register.value = response.body()
                } else {
                    _isLoading.value = false
                    _snackBarText.value = Event(getStringError(context))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = Event("${t.message}")
            }
        })

        return register
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val requestBody1 = email.toRequestBody("text/plain".toMediaType())
            val requestBody2 = password.toRequestBody("text/plain".toMediaType())

            val successResponse = apiService.login(requestBody1, requestBody2)
            val name = successResponse.data.user.name
            val token = successResponse.token
            val isLogin = true

            saveSession(UserModel(name, token, isLogin))

            emit(Result.Success("Thanks ${successResponse.data.user.name} for the Login!"))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error("Sorry ${errorResponse.data.user.name}, please repeat!"))
        }  catch (e: Exception) {
            emit(Result.Error(getStringError(context)))
        }
    }

    private suspend fun saveSession(userModel: UserModel){
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
        fun getInstance(context: Context, apiService: ApiService, preferences: UserPreferences) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(context, apiService, preferences)
            }.also { instance = it }
    }
}