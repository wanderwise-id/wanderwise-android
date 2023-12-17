package com.example.wanderwise.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.utils.Event

class HomeViewModel (private val pRepository: PostRepository): ViewModel() {
    fun getSessionUser(): LiveData<UserModel> {
        return pRepository.getSession().asLiveData()
    }

    var locationData: String? = ""
    var safetyScore: Any? = null
}