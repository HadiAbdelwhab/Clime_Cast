package com.example.climecast

import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherBytTimeResponse
import com.example.climecast.model.WeatherData
import com.example.climecast.model.WeatherResponse
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response

class FakeWeatherRepository : WeatherRepository {
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

    private val favouriteLocationsFlow = MutableStateFlow<List<Location>>(emptyList())

    fun setFavouriteLocations(locations: List<Location>) {
        favouriteLocationsFlow.value = locations
    }

    override fun getFavouriteLocations(): Flow<List<Location>> = favouriteLocationsFlow

    override suspend fun addLocation(location: Location) {
      val currentList = favouriteLocationsFlow.value.toMutableList()
        currentList.add(location)
        favouriteLocationsFlow.value = currentList
    }

    override suspend fun deleteLocation(location: Location) {
        val currentList = favouriteLocationsFlow.value.toMutableList()
        currentList.remove(location)
        favouriteLocationsFlow.value = currentList
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

    private val alertsFlow = MutableStateFlow<List<NotificationItem>>(emptyList())

    fun setAlerts(alerts: List<NotificationItem>) {
        alertsFlow.value = alerts
    }


    override suspend fun insetNotification(notificationItem: NotificationItem) {
        val currentList = alertsFlow.value.toMutableList()
        currentList.add(notificationItem)
        alertsFlow.value = currentList
    }

    override suspend fun deleteAlertByTimestamp(primaryKey: Long) {
        val currentList = alertsFlow.value.toMutableList()
        currentList.removeAll { it.timestamp == primaryKey }
        alertsFlow.value = currentList
    }

    override fun getAlerts(): Flow<List<NotificationItem>> {
        return alertsFlow
    }

}
