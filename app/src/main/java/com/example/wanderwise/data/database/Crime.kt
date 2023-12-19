package com.example.wanderwise.data.database

data class Crime(
    val key: Any? = null,
    val category: Any? = null,
    val date_published: Any? = null,
    val image: Image = Image(),
    val location: Any? = null,
    val summarize: Any? = null,
    val timezone: Any? = null,
    val title: Any? = null
)

data class Image(
    val delete_url: Any? = null,
    val thumb: Any? = null,
    val url: Any? = null
)

data class LocationCrime(
    val windDirection: Any? = null,
    val city: Any? = null
)