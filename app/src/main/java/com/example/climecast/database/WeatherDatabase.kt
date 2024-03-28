package com.example.climecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.climecast.database.dao.FavouriteLocationsDao
import com.example.climecast.database.dao.NotificationDao
import com.example.climecast.database.dao.WeatherDataDao
import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherData


@Database(
    entities = [Location::class, WeatherData::class,
        NotificationItem::class], version = 1
)

abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationDao(): FavouriteLocationsDao
    abstract fun weatherDateDao(): WeatherDataDao
    abstract fun notificationDao(): NotificationDao


    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance

                return instance

            }
        }
    }
}