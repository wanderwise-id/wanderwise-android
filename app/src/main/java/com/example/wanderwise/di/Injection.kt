package com.example.wanderwise.di

import android.content.Context
import com.example.mystoryapp.data.preferences.UserPreferences
import com.example.mystoryapp.data.preferences.dataStore
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.retrofit.ApiConfig
import com.example.wanderwise.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepo(context: Context): PostRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first()}
        val apiService = ApiConfig.getApiService(user.token)

        return PostRepository.getInstance(context, apiService, pref)
    }

}