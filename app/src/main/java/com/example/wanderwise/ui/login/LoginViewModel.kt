package com.example.wanderwise.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.preferences.UserModel

class LoginViewModel (private val pRepository: PostRepository): ViewModel() {

    fun loginUser(email: String, password: String) = pRepository.loginUser(email, password)

    fun getSessionUser(): LiveData<UserModel> {
        return pRepository.getSession().asLiveData()
    }
}