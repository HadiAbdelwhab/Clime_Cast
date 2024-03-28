package com.example.climecast.database

import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    //favourite_location_methods
    fun getFavouriteLocation(): Flow<List<Location>>

    suspend fun addLocation(location: Location)

    suspend fun deleteLocation(location: Location)

    //weather_date_methods
    suspend fun insertWeatherData(weatherData: WeatherData)

    suspend fun deleteWeatherDate()

    fun getWeatherData(): Flow<WeatherData>

    //alerts_method

    suspend fun insetNotification(notificationItem: NotificationItem)

    suspend fun deleteAlertByTimestamp(primaryKey: Long)

    fun getAlerts(): Flow<List<NotificationItem>>
}