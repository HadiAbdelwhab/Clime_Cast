package com.example.climecast.ui.alerts

import com.example.climecast.model.NotificationItem

interface AlertClickListener {
    fun cancelAlert(notificationItem: NotificationItem)
}