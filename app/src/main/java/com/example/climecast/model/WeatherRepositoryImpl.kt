package com.example.climecast.model

import com.example.climecast.network.WeatherRemoteDataSource
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {


    companion object {
        private var instance: WeatherRepositoryImpl? = null

        fun getInstance(
            remoteDataSource:
            WeatherRemoteDataSource
        ): WeatherRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepositoryImpl(
                    remoteDataSource
                ).also { instance = it }
            }
        }
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Flow<Response<WeatherData>> {
        return remoteDataSource.getWeatherForecast(lat,lon)
    }
}