package com.example.climecast.network

import com.example.climecast.model.WeatherData
import com.example.climecast.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Response<WeatherData>
}

