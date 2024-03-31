package com.example.climecast.database

import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSource(
    private val locations: MutableList<Location> = mutableListOf(),
    private val notificationItems: MutableList<NotificationItem> = mutableListOf(),
) : WeatherLocalDataSource {
    override fun getFavouriteLocation(): Flow<List<Location>> {
        return flow { emit(locations) }
    }

    override suspend fun addLocation(location: Location) {
        locations.add(location)
    }

    override suspend fun deleteLocation(location: Location) {
        locations.remove(location)
    }
    override suspend fun insertWeatherData(weatherData: WeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherDate() {
        TODO("Not yet implemented")
    }

    override fun getWeatherData(): Flow<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun insetNotification(notificationItem: NotificationItem) {
        notificationItems.add(notificationItem)
    }

    override suspend fun deleteAlertByTimestamp(primaryKey: Long) {
        notificationItems.removeAll { it.timestamp == primaryKey }
    }

    override fun getAlerts(): Flow<List<NotificationItem>> {
        return flow { emit(notificationItems) }
    }
}