package com.example.climecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.climecast.model.Location
import com.example.climecast.model.WeatherData


@Database(entities = [Location::class, WeatherData::class], version = 1)

abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationDao(): FavouriteLocationsDao
    abstract fun weatherDateDao(): WeatherDataDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "locations_database"
                )
                    .build()
                INSTANCE = instance

                return instance

            }
        }
    }
}