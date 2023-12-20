package com.example.wanderwise.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.utils.Event
import kotlinx.coroutines.launch

class GetPostViewModel(private val pRepository: PostRepository): ViewModel() {

    val uid = pRepository.userUid

    fun getSessionUser(): LiveData<UserModel> {
        return pRepository.getSession().asLiveData()
    }
}