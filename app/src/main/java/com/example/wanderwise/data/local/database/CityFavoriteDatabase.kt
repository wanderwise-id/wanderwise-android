package com.example.wanderwise.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityFavorite::class], version = 1)
abstract class CityFavoriteDatabase: RoomDatabase() {

    abstract fun cityFavDao(): CityFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: CityFavoriteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): CityFavoriteDatabase {
            if (INSTANCE == null) {
                synchronized(CityFavoriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        CityFavoriteDatabase::class.java, "City Database")
                        .build()
                }
            }

            return INSTANCE as CityFavoriteDatabase
        }
    }

}