package com.example.skywatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SkyWatchDaoTest {

    @get:Rule
    var instantExceptionRule = InstantTaskExecutorRule()

    private lateinit var skyWatchDatabase: SkyWatchDatabase

    @Before
    fun setUP()
    {
        skyWatchDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            SkyWatchDatabase::class.java
        ).build()
    }

    @After
    fun after()
    {
        skyWatchDatabase.close()
    }

    @Test
    fun insertLocationToFavorites()= runBlockingTest{
        //Given
        val favoritePojo = FavoritePojo("ITI Ismailia",32.32344,32.23948)

        //When
        skyWatchDatabase.SkyWatchDao().insertFavLocation(favoritePojo)
         val allFav = skyWatchDatabase.SkyWatchDao().getAllFavLocations()

        //Then
        assertThat<List<FavoritePojo>>(allFav as List<FavoritePojo>, notNullValue())
        assertThat(allFav.size , `is`(1))
    }

    @Test
    fun deleteLocationFromFavorites()= runBlockingTest{
        //Given
        val favoritePojo = FavoritePojo("ITI Ismailia",32.32344,32.23948)

        //When
        skyWatchDatabase.SkyWatchDao().insertFavLocation(favoritePojo)

        skyWatchDatabase.SkyWatchDao().delFavLocation(favoritePojo) // Delete Fun
        val allFav = skyWatchDatabase.SkyWatchDao().getAllFavLocations()

        //Then
        assertThat(allFav.size , `is`(0))
    }

    @Test
    fun getAllLocationsFromFavorites()= runBlockingTest{
        //Given //Shouldn't have same Locations lat & lng
        val ism = FavoritePojo("ITI Ismailia",31.32344,32.23948)
        val smart = FavoritePojo("ITI Smart",32.32354,32.23948)
        val alex = FavoritePojo("ITI Alex",33.32364,32.23948)

        //When
        skyWatchDatabase.SkyWatchDao().insertFavLocation(ism)
        skyWatchDatabase.SkyWatchDao().insertFavLocation(smart)
        skyWatchDatabase.SkyWatchDao().insertFavLocation(alex)

        val allFav = skyWatchDatabase.SkyWatchDao().getAllFavLocations()

        //Then
        assertThat(allFav.size , `is`(3))
    }


    @Test
    fun insertLocationToAlerts()= runBlockingTest{
        //Given
        val alertPojo = AlertPojo(lat = 32.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")

        //When
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojo)
        val allAlerts = skyWatchDatabase.SkyWatchDao().getAllAlertLocations()

        //Then
        assertThat<List<AlertPojo>>(allAlerts as List<AlertPojo>, notNullValue())
        assertThat(allAlerts.size , `is`(1))
    }

    @Test
    fun deleteLocationFromAlerts()= runBlockingTest{
        //Given
        val alertPojo = AlertPojo(lat = 32.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")

        //When
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojo)

        skyWatchDatabase.SkyWatchDao().delAlertLocation(alertPojo) // Delete Fun
        val allAlerts = skyWatchDatabase.SkyWatchDao().getAllAlertLocations()

        //Then
        assertThat(allAlerts.size , `is`(0))
    }

    @Test
    fun getAllLocationsFromAlerts()= runBlockingTest{
        //Given
        val alertPojoOne = AlertPojo(lat = 31.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")
        val alertPojoTwo = AlertPojo(lat = 32.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")
        val alertPojoThree = AlertPojo(lat = 33.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")

        //When
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojoOne)
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojoTwo)
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojoThree)

        val allAlerts = skyWatchDatabase.SkyWatchDao().getAllAlertLocations()

        //Then
        assertThat(allAlerts.size , `is`(3))
    }

    @Test
    fun getAlertByID()= runBlockingTest{
        //Given
        val alertPojo = AlertPojo(id = "1", lat = 32.32344, lon = 32.23948, start = 1, end = 1, kind = "ALERT")

        //When
        skyWatchDatabase.SkyWatchDao().insertAlertLocation(alertPojo)
        val alert = skyWatchDatabase.SkyWatchDao().getAlertWithId("1")

        //Then
        assertThat(alert, `is`(alertPojo))
    }
}