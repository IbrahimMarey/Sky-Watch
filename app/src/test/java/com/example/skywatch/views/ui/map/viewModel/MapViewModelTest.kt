package com.example.skywatch.views.ui.map.viewModel

import com.example.skywatch.Fakes.FakeRepo
import com.example.skywatch.models.FavoritePojo
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class MapViewModelTest
{
    lateinit var fakeRepo: FakeRepo

    @Before
    fun setUP()
    {
        fakeRepo = FakeRepo()
    }

    @Test
    fun addLocationToFav() = runBlockingTest{
        // when
        val  favoritePojo = FavoritePojo("ITI Isamilia",32.93029,32.91029)
        val resulte = fakeRepo.insertFavLocation(favoritePojo)

        //then
        assertThat(resulte , `is`(notNullValue()))
        assertThat(resulte , `is`(1))
    }
}