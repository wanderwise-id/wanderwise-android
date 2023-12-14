package com.example.wanderwise.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderwise.data.PostRepository
import kotlinx.coroutines.launch

class ProfileViewModel (private val pRepository: PostRepository): ViewModel() {

    fun logoutUser() {
        viewModelScope.launch {
            pRepository.logout()
        }
    }

}