package com.example.climecast.repository

import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherBytTimeResponse
import com.example.climecast.model.WeatherData
import com.example.climecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FakeWeatherRepository:WeatherRepository {
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

    override fun getFavouriteLocations(): Flow<List<Location>> {
        TODO("Not yet implemented")
    }

    override suspend fun addLocation(location: Location) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocation(location: Location) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherDate(weatherData: WeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherData() {
        TODO("Not yet implemented")
    }

    override fun getWeatherData(): Flow<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun insetNotification(notificationItem: NotificationItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertByTimestamp(primaryKey: Long) {
        TODO("Not yet implemented")
    }

    override fun getAlerts(): Flow<List<NotificationItem>> {
        TODO("Not yet implemented")
    }
}