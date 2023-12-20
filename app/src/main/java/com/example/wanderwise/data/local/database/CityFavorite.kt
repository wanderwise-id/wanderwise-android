package com.example.wanderwise.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cityFav")
@Parcelize
data class CityFavorite(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "key")
    var key: String? = null,

    @ColumnInfo(name = "isLoved")
    var isLoved: Boolean = false
): Parcelable