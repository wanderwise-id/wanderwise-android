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
	val image: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt = CreatedAt(),

	@field:SerializedName("caption")
	val caption: Any? = null,

	@field:SerializedName("id")
	val id: Any? = null,

	@field:SerializedName("title")
	val title: Any? = null,

	@field:SerializedName("idPost")
	val idPost: Any? = null,

	@field:SerializedName("name")
	val name: Any? = null
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Any =0,

	@field:SerializedName("_seconds")
	val seconds: Any = 0
)

data class BodyPostUser(

	@field:SerializedName("posts")
	val posts: List<PostsItem>
)
