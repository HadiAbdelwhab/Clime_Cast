package com.example.climecast.ui.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.model.NotificationItem
import com.example.climecast.model.WeatherResponse
import com.example.climecast.network.ApiState
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(private val _repo: WeatherRepository) : ViewModel() {

    private var _alertsStateFlow: MutableStateFlow<List<NotificationItem>> =
        MutableStateFlow(emptyList())
    val alertsStateFlow = _alertsStateFlow.asStateFlow()

    init {
        getAlerts()
    }

    private fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getAlerts().collect {
                _alertsStateFlow.value = it
            }
        }
    }

    fun insertAlert(notificationItem: NotificationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insetNotification(notificationItem)
        }
    }



}