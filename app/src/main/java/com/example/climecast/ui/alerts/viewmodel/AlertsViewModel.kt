package com.example.climecast.ui.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.model.WeatherResponse
import com.example.climecast.network.ApiState
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(private val _repo: WeatherRepository) : ViewModel() {

    private var _weatherForecastStateFlow: MutableStateFlow<ApiState<WeatherResponse>> =
        MutableStateFlow(ApiState.Loading)
    val weatherForecastByTimeStateFlow = _weatherForecastStateFlow.asStateFlow()

    /*fun getWeatherForecastByTime(lat: Double, lon: Double, timeStamp: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherForecastByTime(lat, lon, timeStamp)
                .catch {

                }.collect { response ->
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        _weatherForecastStateFlow.value = ApiState.Success(weatherResponse)

                    } else {
                        _weatherForecastStateFlow.value =
                            ApiState.Error(NullPointerException("Weather response body is null"))
                    }
                }
        }
    }*/
}