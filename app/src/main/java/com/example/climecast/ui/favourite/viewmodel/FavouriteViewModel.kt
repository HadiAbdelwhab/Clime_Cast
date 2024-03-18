package com.example.climecast.ui.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.database.Location
import com.example.climecast.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val
    repo: WeatherRepository
) : ViewModel() {

    private var _favouriteLocationsStateFlow: MutableStateFlow<List<Location>> =
        MutableStateFlow(emptyList())
    val favouriteLocationsStateFlow = _favouriteLocationsStateFlow.asStateFlow()

    init {
        getFavouriteLocations()
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