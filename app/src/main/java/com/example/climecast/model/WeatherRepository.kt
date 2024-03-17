package com.example.climecast.model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double
    ): Flow<Response<WeatherData>>

}