package com.example.climecast.model

import java.time.LocalDateTime
import java.util.Date

data class AlertDTO(
    val alertTime: LocalDateTime,
    val weatherDescription: String,
)
