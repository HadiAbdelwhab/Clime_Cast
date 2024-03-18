package com.example.climecast.database

import androidx.room.Entity

@Entity(tableName = "favourite_locations_table")
data class Location(
    val city: String,
    val latitude: Double,
    val longitude: Double
)