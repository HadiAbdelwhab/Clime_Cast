package com.example.climecast.database

import android.content.Context
import com.example.climecast.database.dao.FavouriteLocationsDao
import com.example.climecast.database.dao.NotificationDao
import com.example.climecast.database.dao.WeatherDataDao
import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context) : WeatherLocalDataSource {

    private val weatherDataDao: WeatherDataDao
    private val locationsDao: FavouriteLocationsDao
    private val notificationDao: NotificationDao
    private val database: WeatherDatabase

    init {
        database = WeatherDatabase.getInstance(context)
        locationsDao = database.locationDao()
        weatherDataDao = database.weatherDateDao()
        notificationDao = database.notificationDao()
    }

    companion object {
        @Volatile
        private var instance: WeatherLocalDataSourceImpl? = null
        fun getInstance(context: Context): WeatherLocalDataSourceImpl {
            return instance ?: synchronized(this) {
                instance
                    ?: WeatherLocalDataSourceImpl(context).also {
                        instance = it
                    }
            }
        }
    }

    override fun getFavouriteLocation(): Flow<List<Location>> {
        return locationsDao.getFavouriteLocations()
    }

    override suspend fun addLocation(location: Location) {
        return locationsDao.insertLocation(location)
    }

    override suspend fun deleteLocation(location: Location) {
        return locationsDao.deleteLocation(location)
    }

    override suspend fun insertWeatherData(weatherData: WeatherData) {
        return weatherDataDao.insertWeather(weatherData)
    }

    override suspend fun deleteWeatherDate() {
        return weatherDataDao.truncateWeatherDataTable()
    }

    override fun getWeatherData(): Flow<WeatherData> {
        return weatherDataDao.getWeatherData()
    }

    override suspend fun insetNotification(notificationItem: NotificationItem) {
         notificationDao.insertNotification(notificationItem)
    }

    override suspend fun deleteAlertByTimestamp(primaryKey: Long) {
        notificationDao.deleteAlertByTimestamp(primaryKey)
    }


    override fun getAlerts(): Flow<List<NotificationItem>> {
        return notificationDao.getAlerts()
    }
}