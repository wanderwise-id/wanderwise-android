package com.example.wanderwise.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.response.LoginResponse
import com.example.wanderwise.di.Injection
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.ui.login.LoginViewModel
import com.example.wanderwise.ui.post.addpost.AddPostActivity
import com.example.wanderwise.ui.post.addpost.AddPostViewModel
import com.example.wanderwise.ui.profile.ProfileViewModel
import com.example.wanderwise.ui.register.RegisterViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val pRepository: PostRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPostViewModel::class.java)){
            return AddPostViewModel(pRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(pRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(pRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(pRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(pRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepo(context))
            }.also { instance = it }
    }
}