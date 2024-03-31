package com.example.climecast.ui.home.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.climecast.model.WeatherData
import com.example.climecast.network.ApiState
import com.example.climecast.FakeWeatherRepository
import com.example.climecast.repository.WeatherRepository
import com.example.climecast.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: WeatherRepository

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        viewModel = HomeViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test addWeatherData`() = runTest {
        val weatherData = WeatherData("data data data data")
        viewModel.addWeatherData(weatherData)
        val result = repository.getWeatherData().first()
        assertEquals(weatherData, result)
    }

    @Test
    fun `test deleteWeatherData`() = runBlocking {
        // Insert some dummy data first
        val weatherData = WeatherData("data data data data")
        repository.insertWeatherDate(weatherData)

        viewModel.deleteWeatherData()
        val result = repository.getWeatherData().firstOrNull()
        assertEquals(null, result)
    }

    @Test
    fun `test getWeatherLocalData`() = runBlocking {
        val weatherData = WeatherData("data data data data")
        repository.insertWeatherDate(weatherData)

        val result = viewModel.localWeatherStateFlow.take(2).toList().last()
        assertEquals(ApiState.Success(weatherData), result)
    }


}
