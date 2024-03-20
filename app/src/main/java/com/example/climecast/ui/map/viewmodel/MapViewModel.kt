package com.example.climecast.ui.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.database.Location
import com.example.climecast.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val repository: WeatherRepository):ViewModel() {


    fun addLocationToFavourite(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLocation(location)
        }
    }
}