//package com.example.wanderwise.data.response
//
//import com.google.gson.annotations.SerializedName
//
//data class GetUserPostResponse(
//
//	@field:SerializedName("msg")
//	val msg: String,
//
//	@field:SerializedName("nbHits")
//	val nbHits: Int,
//
//	@field:SerializedName("error")
//	val error: Boolean,
//
//	@field:SerializedName("body")
//	val body: BodyUser
//)
//
//data class PostItem(
//
//	@field:SerializedName("image")
//	val image: String,
//
//	@field:SerializedName("createdAt")
//	val createdAt: CreatedAtUser,
//
//	@field:SerializedName("caption")
//	val caption: String,
//
//	@field:SerializedName("id")
//	val id: String,
//
//	@field:SerializedName("title")
//	val title: String,
//
//	@field:SerializedName("userId")
//	val userId: String,
//
//	@field:SerializedName("idPost")
//	val idPost: String
//)
//
//data class CreatedAtUser(
//
//	@field:SerializedName("_nanoseconds")
//	val nanoseconds: Int,
//
//	@field:SerializedName("_seconds")
//	val seconds: Int
//)
//
//data class BodyUser(
//
//	@field:SerializedName("post")
//	val post: List<PostItem>
//)
