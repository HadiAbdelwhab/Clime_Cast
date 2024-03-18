package com.example.climecast.database

import android.content.Context
import kotlinx.coroutines.flow.Flow

class LocationsLocalDataSourceImpl(context: Context) : LocationsLocalDataSource {

    private val doa: FavouriteLocationsDao
    private val database: FavouriteLocationsDatabase

    init {
        database = FavouriteLocationsDatabase.getInstance(context)
        doa = database.locationDao()
    }

    companion object {
        @Volatile
        private var instance: LocationsLocalDataSourceImpl? = null
        fun getInstance(context: Context): LocationsLocalDataSourceImpl {
            return LocationsLocalDataSourceImpl.instance ?: synchronized(this) {
                LocationsLocalDataSourceImpl.instance
                    ?: LocationsLocalDataSourceImpl(context).also {
                        LocationsLocalDataSourceImpl.instance = it
                    }
            }
        }
    }

    override fun getFavouriteLocation(): Flow<List<Location>> {
        return doa.getFavouriteLocations()
    }

    override suspend fun addLocation(location: Location) {
        return doa.insertLocation(location)
    }

    override suspend fun deleteLocation(location: Location) {
        return doa.deleteLocation(location)
    }
}