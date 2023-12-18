package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class GetAllPostResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("nbHits")
	val nbHits: Int,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("body")
	val body: BodyPostUser
)

data class PostsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt,

	@field:SerializedName("caption")
	val caption: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("idPost")
	val idPost: String
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class BodyPostUser(

	@field:SerializedName("posts")
	val posts: List<PostsItem>
)
