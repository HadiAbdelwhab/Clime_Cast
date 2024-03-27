package com.example.climecast.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climecast.repository.WeatherRepository

class HomeViewModelFactory(private val _repo: WeatherRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(_repo) as T


        }else{

            throw IllegalArgumentException("View model class not found")
        }


    }
}