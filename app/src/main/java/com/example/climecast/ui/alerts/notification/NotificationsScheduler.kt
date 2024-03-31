package com.example.climecast.ui.alerts.notification

import com.example.climecast.model.NotificationItem

interface NotificationsScheduler {

    fun schedule(item: NotificationItem)
    fun cancel(item: NotificationItem)
}