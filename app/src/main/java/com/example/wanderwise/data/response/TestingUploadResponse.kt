package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class TestingUploadResponse(

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("message")
	val message: String
)
