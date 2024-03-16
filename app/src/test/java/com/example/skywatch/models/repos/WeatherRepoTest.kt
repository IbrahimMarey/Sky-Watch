package com.example.skywatch.models.repos

import com.example.skywatch.Fakes.FakeLocalDataSource
import com.example.skywatch.Fakes.FakeRemoteDataSource
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherRepoTest{

    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var weatherRepo: WeatherRepo

    @Before
    fun setUP()
    {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        weatherRepo = WeatherRepo.getInstance(fakeRemoteDataSource,fakeLocalDataSource)
    }

    @Test
    fun getWeatherFromApi_byLatAndLng_returnWeatherPojo() = runBlockingTest{
        //When
        val weatherPojo = weatherRepo.getWeather("32.93772","31.1335")
        //Then
        launch {
            weatherPojo.collect {
                assertThat(it , `is`(WeatherPojo()))
            }
        }
    }

    @Test
    fun getAllFavLocations()= runBlockingTest{
        //when
        val favList= weatherRepo.getAllFavLocation()

        launch {
            favList.collect{
                assertThat(it.size , `is`(1))
            }
        }
    }

    @Test
    fun addToFav() = runBlockingTest{
        //Given
        val favoritePojo = FavoritePojo("ITI Ismailia",32.32344,32.23948)

        //when
        val index = weatherRepo.insertFavLocation(favoritePojo)

        //Then
        assertThat(index , `is`(1))
    }
}