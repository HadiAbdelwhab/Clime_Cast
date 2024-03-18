package com.example.climecast.network

import com.example.climecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


private const val TAG = "WeatherRemoteDataSourceImpl"

class WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {


    private val weatherService: WeatherService by lazy {
        RetrofitHelper.getInstance().create(WeatherService::class.java)
    }

    companion object {
        @Volatile
        private var instance: WeatherRemoteDataSourceImpl? = null

        fun getInstance(): WeatherRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: WeatherRemoteDataSourceImpl().also { instance = it }
            }
        }
    }


    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double
    ): Flow<Response<WeatherResponse>> {
        return flow { emit(weatherService.getWeatherForecast(lat, lon)) }
    }
}