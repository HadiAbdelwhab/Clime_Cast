package com.example.climecast.database

import kotlinx.coroutines.flow.Flow

interface LocationsLocalDataSource {

    fun getFavouriteLocation(): Flow<List<Location>>

    suspend fun addLocation(location: Location)

    suspend fun deleteLocation(location: Location)
}