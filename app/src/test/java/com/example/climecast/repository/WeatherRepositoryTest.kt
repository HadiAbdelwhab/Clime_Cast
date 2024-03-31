package com.example.climecast.repository

import com.example.climecast.database.FakeWeatherLocalDataSource
import com.example.climecast.database.WeatherLocalDataSource
import com.example.climecast.model.Location
import com.example.climecast.model.NotificationItem
import com.example.climecast.network.FakeWeatherRemoteDateSource
import com.example.climecast.network.WeatherRemoteDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    lateinit var localDataSource: WeatherLocalDataSource
    lateinit var remoteDataSource: WeatherRemoteDataSource
    lateinit var repository: WeatherRepository


    @Before
    fun setUp() {

        localDataSource = FakeWeatherLocalDataSource()
        remoteDataSource = FakeWeatherRemoteDateSource()
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSource)
    }

    @Test
    fun testAddLocation() = runTest {
        val location = Location("City", 1234.5678, 9876.5432)
        repository.addLocation(location)

        val result = localDataSource.getFavouriteLocation().single()
        assertEquals(result.size, 1)
    }

    @Test
    fun testDeleteLocation() =runTest{
        val location = Location("City",  1234.5678, 9876.5432)
        repository.addLocation(location)

        repository.deleteLocation(location)

        val result = localDataSource.getFavouriteLocation().single()
        assertEquals(result.size,0)
    }


    @Test
    fun testInsertNotification() = runBlocking {
        val notificationItem = NotificationItem("Desc", "icon",3.0,123456789)
        repository.insetNotification(notificationItem)

        val result = localDataSource.getAlerts().single()
        assertEquals(result.size, 1)
        assertEquals(result[0], notificationItem)
    }

    @Test
    fun testDeleteAlertByTimestamp() = runBlocking {
        val notificationItem = NotificationItem("Desc", "icon",3.0,123456789)
        repository.insetNotification(notificationItem)

        repository.deleteAlertByTimestamp(123456789)

        val result = localDataSource.getAlerts().single()
        assertEquals(result.size, 0)
    }
}

