package com.example.climecast.network

import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface WeatherRemoteDataSource {

    suspend fun getWeatherForecast(lat: Double, lon: Double): Flow<Response<WeatherData>>
}