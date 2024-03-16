package com.example.skywatch.views.ui.home.viewModel

import com.example.skywatch.Fakes.FakeRepo
import com.example.skywatch.models.WeatherPojo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest{

    lateinit var fakeRepo: FakeRepo


    @Before
    fun setUP()
    {
        fakeRepo = FakeRepo()
    }

    @Test
    fun getWeatherData_apiRequest() = runBlockingTest{
        // When
        val resulte = fakeRepo.getWeather("","")

        //then
        assertThat(resulte , `is`(notNullValue()))

    }
}