package com.example.climecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts_table")
data class NotificationItem(
    val description: String,
    val icon: String,
    val temperature: Double,
    @PrimaryKey
    val timestamp: Long
)