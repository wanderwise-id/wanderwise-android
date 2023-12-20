package com.example.wanderwise.data.database

data class City(
	val key: Any? = null,
	val area: Any? = null,
	val capital: Any? = null,
	val country: Any? = null,
	val description: Any? = null,
	val image: Any? = null,
	val location: LocationCity = LocationCity()
)

data class LocationCity(
	val lat: Any? = null,
	val lon: Any? = null
)
