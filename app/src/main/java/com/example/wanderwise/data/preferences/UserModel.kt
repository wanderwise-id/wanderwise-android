package com.example.wanderwise.data.preferences

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)