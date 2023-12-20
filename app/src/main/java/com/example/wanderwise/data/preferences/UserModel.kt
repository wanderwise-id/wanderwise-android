package com.example.wanderwise.data.preferences

import android.net.Uri

data class UserModel(
    val name: String,
    val token: String,
    val email: String,
    val uid: String,
    val userLocation: String,
    val currentActivity: String,
    val isLogin: Boolean = false
)