package com.example.climecast.ui.alerts.notification

interface NotificationsScheduler {

    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}