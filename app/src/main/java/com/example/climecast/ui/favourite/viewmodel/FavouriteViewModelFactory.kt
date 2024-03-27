package com.example.climecast.ui.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climecast.repository.WeatherRepository

class FavouriteViewModelFactory (private val _repo: WeatherRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(_repo) as T


        }else{

            throw IllegalArgumentException("View model class not found")
        }


    }
}