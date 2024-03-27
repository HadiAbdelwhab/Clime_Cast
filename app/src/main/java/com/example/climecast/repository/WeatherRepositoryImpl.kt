package com.example.climecast.repository

import com.example.climecast.model.Location
import com.example.climecast.model.WeatherData
import com.example.climecast.database.WeatherLocalDataSource
import com.example.climecast.model.WeatherResponse
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

    //remote_data_source_methods
    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language: String
    ): Flow<Response<WeatherResponse>> {
        return remoteDataSource.getWeatherForecast(lat, lon, language)
    }

    override suspend fun getWeatherForecastByTime(
        lat: Double,
        lon: Double,
        timeStamp: Long
    ): Flow<Response<WeatherResponse>> {
        return remoteDataSource.getWeatherForecastByTime(lat, lon, timeStamp)
    }


    //local_data_source_methods
    //Location
    override fun getFavouriteLocations(): Flow<List<Location>> {
        return weatherLocalDataSource.getFavouriteLocation()
    }

    override suspend fun addLocation(location: Location) {
        weatherLocalDataSource.addLocation(location)
    }

    override suspend fun deleteLocation(location: Location) {
        weatherLocalDataSource.deleteLocation(location)
    }

    //weather
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