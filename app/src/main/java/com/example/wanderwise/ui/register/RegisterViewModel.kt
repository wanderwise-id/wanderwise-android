package com.example.wanderwise.ui.register

import androidx.lifecycle.ViewModel
import com.example.wanderwise.data.PostRepository

class RegisterViewModel(private val pRepository: PostRepository): ViewModel() {

    fun registerUser(name: String, email: String, password: String) = pRepository.registerUser(name, email, password)

}