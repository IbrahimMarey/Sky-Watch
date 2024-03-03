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
    suspend fun getWeather(@Query("lat") lat :String,@Query("lon") lon:String , @Query("appid")appID:String = Constants.API_KEY): WeatherPojo
}
object RetrofitHelper{
    val retrofitInstance= Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service : ApiService by lazy {
        retrofitInstance.create(ApiService::class.java)
    }
}
/*
*
  @GET("onecall?")
    suspend fun getCurrentWeather(@Query("lat") lat: String?,
                                  @Query("lon") lon: String?,
                                  @Query("appid") appId:String=Constants.APPID,
                                  @Query("lang") lang:String,
                                  @Query("units") units:String ): Welcome
    @GET("onecall?")
    suspend fun getCurrentWeatherTCallBack(@Query("lat") lat: String?,
                                  @Query("lon") lon: String?,
                                  @Query("appid") appId:String=Constants.APPID,
                                  @Query("lang") lang:String,
                                  @Query("units") units:String ): Response<Welcome>
                                  * */