package com.example.climecast.model

import com.example.climecast.database.Location
import com.example.climecast.database.WeatherData
import com.example.climecast.database.WeatherLocalDataSource
import com.example.climecast.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherRepository {


    companion object {
        private var instance: WeatherRepositoryImpl? = null

        fun getInstance(
            remoteDataSource:
            WeatherRemoteDataSource,
            locationLocalDataSource:
            WeatherLocalDataSource
        ): WeatherRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepositoryImpl(
                    remoteDataSource,
                    locationLocalDataSource
                ).also { instance = it }
            }
        }
    }

    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String
    ): Flow<Response<WeatherResponse>> {
        return remoteDataSource.getWeatherForecast(lat, lon, language)
    }

    override fun getFavouriteLocations(): Flow<List<Location>> {
        return weatherLocalDataSource.getFavouriteLocation()
    }

    override suspend fun addLocation(location: Location) {
        weatherLocalDataSource.addLocation(location)
    }

    override suspend fun deleteLocation(location: Location) {
        weatherLocalDataSource.deleteLocation(location)
    }

    override suspend fun insertWeatherDate(weatherData: WeatherData) {
        weatherLocalDataSource.insertWeatherData(weatherData)
    }

    override suspend fun deleteWeatherData() {
        weatherLocalDataSource.deleteWeatherDate()
    }

    override fun getWeatherData(): Flow<WeatherData> {
        return weatherLocalDataSource.getWeatherData()
    }
}