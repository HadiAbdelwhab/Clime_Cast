package com.example.climecast.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.model.WeatherData
import com.example.climecast.model.WeatherResponse
import com.example.climecast.repository.WeatherRepository
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

    private var _localWeatherStateFlow: MutableStateFlow<ApiState<WeatherData>> =
        MutableStateFlow(ApiState.Loading)

    val localWeatherStateFlow = _localWeatherStateFlow.asStateFlow()

    init {
        getWeatherLocalData()
    }

    fun getWeatherForecast(lat: Double, lon: Double, language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherForecast(lat, lon, language)
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


    fun addWeatherData(weatherData: WeatherData) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeatherDate(weatherData)
        }
    }

    fun deleteWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteWeatherData()
        }
    }

    private fun getWeatherLocalData() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherData()
                .collect {
                    _localWeatherStateFlow.value = ApiState.Success(it)
                }
        }
    }
}