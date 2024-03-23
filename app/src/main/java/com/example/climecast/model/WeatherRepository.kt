package com.example.climecast.model

import com.example.climecast.database.Location
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        language:String
    ): Flow<Response<WeatherResponse>>


    fun getFavouriteLocations(): Flow<List<Location>>
    suspend fun addLocation(location: Location)
    suspend fun deleteLocation(location: Location)
}