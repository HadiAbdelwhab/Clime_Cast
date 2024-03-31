package com.example.climecast.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.climecast.database.dao.FavouriteLocationsDao
import com.example.climecast.database.dao.NotificationDao
import com.example.climecast.database.dao.WeatherDataDao
import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var weatherDataDao: WeatherDataDao
    private lateinit var locationsDao: FavouriteLocationsDao
    private lateinit var notificationDao: NotificationDao
    private lateinit var localDataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        weatherDataDao = database.weatherDateDao()
        locationsDao = database.locationDao()
        notificationDao = database.notificationDao()

        localDataSource = WeatherLocalDataSourceImpl(
            ApplicationProvider.getApplicationContext()
        )
    }

    @After
    fun closeDatabase() = database.close()


    @Test
    fun addLocation() = runBlocking {
        val location = Location("City",1.0,1.0)
        localDataSource.addLocation(location)


        val result = locationsDao.getFavouriteLocations().firstOrNull()
        assertEquals(location, result?.first())
    }
    @Test
    fun deleteLocation() = runBlocking {
        val location = Location("City",1.0,1.0)
        locationsDao.insertLocation(location)

        localDataSource.deleteLocation(location)

        val result = locationsDao.getFavouriteLocations().firstOrNull()
        assertEquals(0, result?.size)
    }

    @Test
    fun insertWeatherData() = runBlocking {
        val weatherData = WeatherData("Data data  data data data")
        localDataSource.insertWeatherData(weatherData)

        val result = weatherDataDao.getWeatherData().firstOrNull()
        assertEquals(weatherData, result)
    }

    @Test
    fun deleteWeatherData() = runBlocking {
        val weatherData = WeatherData("Data data  data data data")
        weatherDataDao.insertWeather(weatherData)

        localDataSource.deleteWeatherDate()

        val result = weatherDataDao.getWeatherData().firstOrNull()
        assertEquals(null, result)
    }

    @Test
    fun insertNotification() = runBlocking {
        val notificationItem = NotificationItem("desc", "icon", 15.0, 5616516.0.toLong())
        localDataSource.insetNotification(notificationItem)

        val result = notificationDao.getAlerts().firstOrNull()
        assertEquals(notificationItem, result?.first())
    }

    @Test
    fun deleteAlertByTimestamp() = runBlocking {
        val notificationItem = NotificationItem("desc", "icon", 15.0, 5616516.0.toLong())
        notificationDao.insertNotification(notificationItem)

        localDataSource.deleteAlertByTimestamp(notificationItem.timestamp)

        val result = notificationDao.getAlerts().firstOrNull()
        assertEquals(0, result?.size)
    }

}


