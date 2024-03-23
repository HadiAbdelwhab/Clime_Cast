package com.example.climecast.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.model.WeatherResponse
import com.example.climecast.model.WeatherRepository
import com.example.climecast.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo: WeatherRepository) : ViewModel() {

    private var _weatherForecastStateFlow: MutableStateFlow<ApiState<WeatherResponse>> =
        MutableStateFlow(ApiState.Loading)

    val weatherForecastStateFlow = _weatherForecastStateFlow.asStateFlow()


    fun getWeatherForecast(lat: Double, lon: Double,language:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherForecast(lat, lon,language)
                .catch { error ->

                    _weatherForecastStateFlow.value = ApiState.Error(error)
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
    }
}