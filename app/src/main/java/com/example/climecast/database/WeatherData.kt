package com.example.climecast.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data_table")
data class WeatherData(
    @PrimaryKey
    val dataGson: String
)