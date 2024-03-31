package com.example.climecast.network

import com.example.climecast.model.WeatherBytTimeResponse
import com.example.climecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FakeWeatherRemoteDateSource:WeatherRemoteDataSource {
    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String
    ): Flow<Response<WeatherResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherForecastByTime(
        lat: Double,
        lon: Double,
        timeStamp: Long
    ): Response<WeatherBytTimeResponse> {
        TODO("Not yet implemented")
    }
}