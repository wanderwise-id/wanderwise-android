package com.example.wanderwise.ui.post.addpost

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wanderwise.data.PostRepository
import com.example.wanderwise.utils.Event
import java.io.File

class AddPostViewModel(private val postRepository: PostRepository): ViewModel() {
    val snackbar: LiveData<Event<String>> = postRepository.snackBarText

    val isLoading: LiveData<Boolean> = postRepository.isLoading
    fun uploadImage(imageFile: File, name: String, price: Int) = postRepository.uploadImage(imageFile, name, price)
    fun uploadImageTry(image: File) = postRepository.uploadImage(image)
}