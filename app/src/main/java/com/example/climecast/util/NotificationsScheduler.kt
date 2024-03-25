package com.example.climecast.util

import com.example.climecast.model.AlertDTO
import java.time.LocalDateTime

interface NotificationsScheduler {

    fun schedule(item: LocalDateTime)
    fun cancel(item: LocalDateTime)
}