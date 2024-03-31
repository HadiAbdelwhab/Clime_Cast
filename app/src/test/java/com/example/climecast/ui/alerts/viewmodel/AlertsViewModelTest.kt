package com.example.climecast.ui.alerts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.climecast.model.NotificationItem
import com.example.climecast.FakeWeatherRepository
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlertsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: AlertsViewModel


    lateinit var repository: WeatherRepository

    @Before
    fun setup() {

        repository = FakeWeatherRepository()
        viewModel = AlertsViewModel(repository)

    }


    @Test
    fun insertAlert() = runBlocking {
        // Given
        val notificationItem = NotificationItem("Test", "icon", 25.0, System.currentTimeMillis())

        // When
        viewModel.insertAlert(notificationItem)
        // Then
        val alerts = viewModel.alertsStateFlow.first()
        assertThat(alerts.size, `is`(notNullValue()))
        assertThat(alerts.size, `is`(equalTo(1)))
    }

    @Test
    fun deleteAlert() = runBlocking {
        // Given
        val notificationItem = NotificationItem("Test", "icon", 25.0, System.currentTimeMillis())

        //when
        viewModel.insertAlert(notificationItem)

        viewModel.deleteAlert(notificationItem)

        //then
        val updatedAlerts = viewModel.alertsStateFlow.first()
        assertThat(updatedAlerts.size, `is`(equalTo(0)))
    }

}
