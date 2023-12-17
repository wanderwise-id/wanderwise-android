package com.example.wanderwise.ui.post.addpost

import androidx.lifecycle.ViewModel
import com.example.wanderwise.data.PostRepository
import java.io.File

class AddPostViewModel(private val postRepository: PostRepository): ViewModel() {

    fun uploadImage(title: String, caption: String, imageFile: File) = postRepository.uploadImage(title, caption, imageFile)
}