package com.example.climecast.database

import android.content.Context
import com.example.climecast.model.Location
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context) : WeatherLocalDataSource {

    private val weatherDataDao: WeatherDataDao
    private val locationsDao: FavouriteLocationsDao
    private val database: WeatherDatabase

    init {
        database = WeatherDatabase.getInstance(context)
        locationsDao = database.locationDao()
        weatherDataDao = database.weatherDateDao()
    }

    companion object {
        @Volatile
        private var instance: WeatherLocalDataSourceImpl? = null
        fun getInstance(context: Context): WeatherLocalDataSourceImpl {
            return WeatherLocalDataSourceImpl.instance ?: synchronized(this) {
                WeatherLocalDataSourceImpl.instance
                    ?: WeatherLocalDataSourceImpl(context).also {
                        WeatherLocalDataSourceImpl.instance = it
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
}