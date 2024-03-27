package com.example.climecast.network

import com.example.climecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface WeatherRemoteDataSource {

    suspend fun getWeatherForecast(
        lat: Double, lon: Double, language: String
    ): Flow<Response<WeatherResponse>>

    suspend fun getWeatherForecastByTime(
        lat: Double, lon: Double, timeStamp: Long
    ): Flow<Response<WeatherResponse>>
}