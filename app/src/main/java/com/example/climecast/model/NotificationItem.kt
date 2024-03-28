package com.example.climecast.model


data class NotificationItem(
    val description: String,
    val icon: String,
    val temperature: Double,
    val timestamp: Long
)