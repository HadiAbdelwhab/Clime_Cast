package com.example.climecast.model

import java.util.Date

data class AlertDTO(
    val startTime: Date,
    val endTime: Date,
    val startDate: Date,
    val endDate: Date
)
