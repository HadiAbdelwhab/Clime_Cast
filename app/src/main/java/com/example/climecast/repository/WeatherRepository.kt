package com.example.climecast.repository

import com.example.climecast.model.Location
import com.example.climecast.model.WeatherBytTimeResponse
import com.example.climecast.model.WeatherData
import com.example.climecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String
    ): Flow<Response<WeatherResponse>>

    suspend fun getWeatherForecastByTime(
        lat: Double,
        lon: Double,
        timeStamp: Long
    ): Response<WeatherBytTimeResponse>

    fun getFavouriteLocations(): Flow<List<Location>>
    suspend fun addLocation(location: Location)
    suspend fun deleteLocation(location: Location)
    suspend fun insertWeatherDate(weatherData: WeatherData)
    suspend fun deleteWeatherData()
    fun getWeatherData(): Flow<WeatherData>
}