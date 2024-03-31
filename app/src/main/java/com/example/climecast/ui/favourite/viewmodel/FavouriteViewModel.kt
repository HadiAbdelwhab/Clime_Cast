package com.example.climecast.ui.favourite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.model.Location
import com.example.climecast.model.WeatherResponse
import com.example.climecast.network.ApiState
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private const val TAG = "FavouriteViewModel"
class FavouriteViewModel(
    private val
    repo: WeatherRepository
) : ViewModel() {
    private var _weatherForecastStateFlow: MutableStateFlow<ApiState<WeatherResponse>> =
        MutableStateFlow(ApiState.Loading)

    val weatherForecastStateFlow = _weatherForecastStateFlow.asStateFlow()

    private var _favouriteLocationsStateFlow: MutableStateFlow<List<Location>> =
        MutableStateFlow(emptyList())
    val favouriteLocationsStateFlow = _favouriteLocationsStateFlow.asStateFlow()

    init {
        getFavouriteLocations()
    }

    fun getWeatherForecast(lat: Double, lon: Double, language: String) {
        Log.i(TAG, "getWeatherForecast: ")
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWeatherForecast(lat, lon, language)
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


    private fun getFavouriteLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavouriteLocations()
                .collect {
                    _favouriteLocationsStateFlow.value = it
                }
        }
    }

    fun addLocationToFavourite(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addLocation(location)
        }
    }

    fun deleteLocationFromFavourite(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteLocation(location)
        }
    }

}