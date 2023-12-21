package com.example.wanderwise.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CityFavoriteDao {

    @Query("SELECT * FROM cityFav ORDER BY `key` ASC")
    fun getCity(): LiveData<List<CityFavorite>>

    @Query("SELECT * FROM cityFav WHERE `key` = :city")
    fun getCityClicked(city: String): CityFavorite

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFav(userFavorite: CityFavorite)

    @Update
    fun updateFav(userFavorite: CityFavorite)

    @Query("DELETE FROM cityFav WHERE `key` = :city")
    fun deleteFav(city: String)

}