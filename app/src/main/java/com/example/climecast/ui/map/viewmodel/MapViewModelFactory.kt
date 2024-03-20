package com.example.climecast.ui.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climecast.model.WeatherRepository
import com.example.climecast.ui.favourite.viewmodel.FavouriteViewModel

class MapViewModelFactory(private val _repo: WeatherRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(_repo) as T


        }else{

            throw IllegalArgumentException("View model class not found")
        }


    }
}