package com.example.climecast.network

import com.example.climecast.model.WeatherBytTimeResponse
import com.example.climecast.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String
    ): Response<WeatherResponse>

    @GET("onecall/timemachine")
    suspend fun getWeatherForecastByTime(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") timeStamp: Long
    ): Response<WeatherBytTimeResponse>
}

