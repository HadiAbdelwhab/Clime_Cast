package com.example.climecast.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {

    @Insert
    suspend fun insertWeather(weatherData: WeatherData)

    @Query("DELETE FROM weather_data_table")
    suspend fun truncateWeatherDataTable()

    @Query("select * from weather_data_table LIMIT 1")
    fun getWeatherData(): Flow<WeatherData>
}
