package com.example.wanderwise.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cityFav")
@Parcelize
data class CityFavorite(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "key")
    val key: String? = null

): Parcelable