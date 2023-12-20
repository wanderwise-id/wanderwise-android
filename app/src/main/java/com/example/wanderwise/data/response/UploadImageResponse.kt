package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("body")
	val body: BodyPost
)

data class CreatedAtTime(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class Post(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAtTime,

	@field:SerializedName("caption")
	val caption: String,

	@field:SerializedName("city")
	val title: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("idPost")
	val idPost: String
)

data class BodyPost(

	@field:SerializedName("post")
	val post: Post
)
