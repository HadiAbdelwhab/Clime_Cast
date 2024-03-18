package com.example.climecast.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_locations_table")
data class Location(
    val city: String,
    @PrimaryKey
    val latitude: Double,
    val longitude: Double
)