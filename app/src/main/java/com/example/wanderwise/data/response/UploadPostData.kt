package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName
import java.io.File

data class UploadPostData(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error")
	val error: Boolean
)

data class DataItem(

	@field:SerializedName("image")
	val image: File,

	@field:SerializedName("price")
	val price: Any,

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String
)
