package com.example.climecast.database

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.climecast.model.WeatherData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceTest {

    private lateinit var dataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        val context: Application = ApplicationProvider.getApplicationContext()
        dataSource = WeatherLocalDataSourceImpl(context)
    }

    @Test
    fun `test insert and get WeatherData`() = runBlocking {
        val weatherData = WeatherData("data data data data")
        dataSource.insertWeatherData(weatherData)

        val result = dataSource.getWeatherData().firstOrNull()
        assertEquals(weatherData, result)
    }

    @Test
    fun `test delete WeatherData`() = runBlocking {
        val weatherData = WeatherData("data data data data")
        dataSource.insertWeatherData(weatherData)

        dataSource.deleteWeatherDate()

        val result = dataSource.getWeatherData().firstOrNull()
        assertEquals(null, result)
    }
}
