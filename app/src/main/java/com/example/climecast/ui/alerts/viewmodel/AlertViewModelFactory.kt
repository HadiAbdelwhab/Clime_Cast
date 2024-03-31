package com.example.climecast.ui.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climecast.repository.WeatherRepository

class AlertViewModelFactory(private val _repo: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            AlertsViewModel(_repo) as T


        } else {

            throw IllegalArgumentException("View model class not found")
        }


    }

}