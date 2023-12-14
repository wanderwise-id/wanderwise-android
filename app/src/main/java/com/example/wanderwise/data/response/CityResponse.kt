package com.example.wanderwise.data.response

import com.google.gson.annotations.SerializedName

data class CityResponse(
	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("population")
	val population: Int
)
