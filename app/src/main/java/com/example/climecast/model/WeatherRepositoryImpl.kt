package com.example.climecast.model

import com.example.climecast.database.Location
import com.example.climecast.database.LocationsLocalDataSource
import com.example.climecast.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val locationLocalDataSource: LocationsLocalDataSource
) : WeatherRepository {


    companion object {
        private var instance: WeatherRepositoryImpl? = null

        fun getInstance(
            remoteDataSource:
            WeatherRemoteDataSource,
            locationLocalDataSource:
            LocationsLocalDataSource
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
        lon: Double
    ): Flow<Response<WeatherResponse>> {
        return remoteDataSource.getWeatherForecast(lat, lon)
    }

    override fun getFavouriteLocations(): Flow<List<Location>> {
        return locationLocalDataSource.getFavouriteLocation()
    }

    override suspend fun addLocation(location: Location) {
        locationLocalDataSource.addLocation(location)
    }

    override suspend fun deleteLocation(location: Location) {
        locationLocalDataSource.deleteLocation(location)
    }
}