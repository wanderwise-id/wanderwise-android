package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class UploadPhotoResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("body")
	val body: BodyUpload
)

data class BodyUpload(

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("email")
	val email: String
)
