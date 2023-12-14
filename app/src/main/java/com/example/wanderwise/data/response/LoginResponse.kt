package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("token")
	val token: String
)

data class Data(

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)
