package com.example.climecast.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.climecast.database.WeatherDatabase
import com.example.climecast.model.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavouriteLocationsDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: FavouriteLocationsDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.locationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertLocation() = runBlocking {
        val location = Location("city",90.0,90.0)
        dao.insertLocation(location)

        val result = dao.getFavouriteLocations().firstOrNull()
        assertEquals(1, result?.size)
        assertEquals(location, result?.get(0))
    }

    @Test
    fun getFavouriteLocations() = runBlocking {
        val location1 = Location("city1",90.0,90.0)
        val location2 = Location("city2",90.0,90.0)
        dao.insertLocation(location1)
        dao.insertLocation(location2)

        val result = dao.getFavouriteLocations().firstOrNull()
        assertEquals(2, result?.size)
        assertEquals(location1, result?.get(0))
        assertEquals(location2, result?.get(1))
    }

    @Test
    fun deleteLocation() = runBlocking {
        val location = Location("city",90.0,90.0)
        dao.insertLocation(location)

        dao.deleteLocation(location)

        val result = dao.getFavouriteLocations().firstOrNull()
        assertEquals(0, result?.size)
    }
}
