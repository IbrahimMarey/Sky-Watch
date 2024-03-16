package com.example.skywatch.Fakes

import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.models.repos.WeatherRepoInterface
import com.example.skywatch.models.status.HomeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class FakeRepo : WeatherRepoInterface {

    private val weather: MutableStateFlow<HomeStatus> = MutableStateFlow<HomeStatus>(HomeStatus.Loading)
    val weatherData: StateFlow<HomeStatus> = weather
    override suspend fun getWeather(lat: String, lon: String): Flow<WeatherPojo> {
        weather.value = HomeStatus.Success(WeatherPojo())
        return weatherData.map { status ->
            when (status) {
                is HomeStatus.Success -> status.weatherPojo
                else -> throw IllegalStateException("Invalid state")
            }
        }
    }

    override suspend fun getAllFavLocation(): Flow<List<FavoritePojo>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavLocation(favoritePojo: FavoritePojo): Long {

        return 1
    }

    override suspend fun delFavLocation(favoritePojo: FavoritePojo): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlertLocation(alertPojo: AlertPojo): Long {
        TODO("Not yet implemented")
    }

    override suspend fun delAlertLocation(alertPojo: AlertPojo): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAlertLocations(): Flow<List<AlertPojo>> {
        TODO("Not yet implemented")
    }

    override fun getAlertWithId(entryId: String): AlertPojo {
        TODO("Not yet implemented")
    }
}