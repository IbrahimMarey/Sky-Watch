package com.example.skywatch.remote

import com.example.skywatch.Constants
import com.example.skywatch.models.WeatherPojo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService
{
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat :String,
        @Query("lon") lon:String ,
        @Query("appid")appID:String = Constants.API_KEY ,
        @Query("exclude") exclude:String = Constants.EXCLUDE,
        @Query("units") units:String = Constants.UNITS): WeatherPojo
}
object RetrofitHelper{
    private val retrofitInstance= Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service : ApiService by lazy {
        retrofitInstance.create(ApiService::class.java)
    }
}