package com.example.wanderwise.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.preferences.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel (private val pRepository: PostRepository): ViewModel() {

    fun logoutUser() {
        viewModelScope.launch {
            pRepository.logout()
        }
    }

}