package com.example.climecast.database

import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getFavouriteLocation(): Flow<List<Location>>

    suspend fun addLocation(location: Location)

    suspend fun deleteLocation(location: Location)

    suspend fun insertWeatherData(weatherData: WeatherData)

    suspend fun deleteWeatherDate()

    fun getWeatherData():Flow<WeatherData>
}