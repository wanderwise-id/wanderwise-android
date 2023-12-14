package com.example.wanderwise.data.retrofit

import com.example.wanderwise.data.response.CityResponse
import com.example.wanderwise.data.response.LoginResponse
import com.example.wanderwise.data.response.RegisterResponse
import com.example.wanderwise.data.response.TestingUploadResponse
import com.example.wanderwise.data.response.UploadPostData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ApiService {

    @Multipart
    @POST("posts/uploads")
    fun uploadPost(
        @Part image: MultipartBody.Part,
        @Part("title") name: RequestBody,
        @Part("description") description: RequestBody
    ): Call<UploadPostData>

    @Multipart
    @POST("uploadcloud")
    fun testUpload(
        @Part file: MultipartBody.Part
    ): Call<TestingUploadResponse>

    @Multipart
    @POST("auth/register")
    fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Call<RegisterResponse>

    @Multipart
    @POST("auth/login")
    suspend fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): LoginResponse
}