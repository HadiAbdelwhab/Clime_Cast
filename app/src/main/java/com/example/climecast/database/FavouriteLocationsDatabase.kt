package com.example.climecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Location::class], version = 1)

abstract class FavouriteLocationsDatabase : RoomDatabase() {

    abstract fun locationDao(): FavouriteLocationsDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteLocationsDatabase? = null
        fun getInstance(context: Context): FavouriteLocationsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteLocationsDatabase::class.java,
                    "locations_database"
                )
                    .build()
                INSTANCE = instance

                return instance

            }
        }
    }
}