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
import com.example.wanderwise.data.response.UploadImageResponse
import com.example.wanderwise.data.retrofit.ApiConfig
import com.example.wanderwise.data.retrofit.ApiService
import com.example.wanderwise.utils.Event
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class PostRepository private constructor
(
    private val context: Context,
    private val apiService: ApiService,
    private val preferences: UserPreferences
){

    fun uploadImage(title: String, caption: String, image: File) = liveData {
        emit(Result.Loading)

        try {
            val titleRequestBody = title.toRequestBody("plain/text".toMediaType())
            val captionRequestBody = caption.toRequestBody("plain/text".toMediaType())
            val requestImageFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
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
            val isLogin = true
            Log.d("tokenCheck", token)
            saveSession(UserModel(name, token, emailUser, isLogin))
            emit(Result.Success("Thanks ${successResponse.body.name} for the Login!"))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error("Sorry, ${errorResponse.msg}, please repeat!"))
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