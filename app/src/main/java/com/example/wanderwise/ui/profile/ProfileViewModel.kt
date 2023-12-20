package com.example.wanderwise.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.data.response.UploadImageResponse
import com.example.wanderwise.data.response.UploadPhotoResponse
import com.example.wanderwise.utils.Event
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel (private val pRepository: PostRepository): ViewModel() {
    fun getSessionUser(): LiveData<UserModel> {
        return pRepository.getSession().asLiveData()
    }

    val snackbar: LiveData<Event<String>> = pRepository.snackBarText

    val isLoading: LiveData<Boolean> = pRepository.isLoading

    fun getPhoto(): LiveData<UploadPhotoResponse> = pRepository.getImage()

    fun uploadPhotoProfile(image: File) = pRepository.uploadPhotoUser(image)

    fun logoutUser() {
        viewModelScope.launch {
            pRepository.logout()
        }
    }

    fun editUserModel(userModel: UserModel) {
        viewModelScope.launch {
            pRepository.saveSession(userModel)
        }
    }

}