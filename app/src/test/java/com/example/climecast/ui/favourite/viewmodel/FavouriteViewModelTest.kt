package com.example.climecast.ui.favourite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.climecast.FakeWeatherRepository
import com.example.climecast.model.Location
import com.example.climecast.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavouriteViewModel
    private lateinit var fakeRepository: WeatherRepository

    @Before
    fun setup() {
        fakeRepository = FakeWeatherRepository()
        viewModel = FavouriteViewModel(fakeRepository)
    }

    @Test
    fun addLocationToFavourite() = runBlockingTest {
        // Given a location
        val location = Location("New York", 40.7128, -74.0060)

        // When adding the location to favourites
        viewModel.addLocationToFavourite(location)

        // Then the location should be added to the repository
        val favourites = viewModel.favouriteLocationsStateFlow.first()
        MatcherAssert.assertThat(favourites, CoreMatchers.hasItem(location))
    }

    @Test
    fun deleteLocationFromFavourite() = runBlockingTest {
        // Given a location
        val location = Location("New York", 40.7128, -74.0060)

        // Add the location to favourites
        viewModel.addLocationToFavourite(location)

        // When deleting the location from favourites
        viewModel.deleteLocationFromFavourite(location)

        // Then the location should be removed from the repository
        val favourites = viewModel.favouriteLocationsStateFlow.first()
        MatcherAssert.assertThat(favourites, CoreMatchers.not(CoreMatchers.hasItem(location)))
    }

    @Test
    fun adddition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }
}
