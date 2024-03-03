package com.example.skywatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.home.viewModel.HomeViewModel
import com.example.skywatch.views.home.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(RetrofitHelper))
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.getWeather("30.1771","31.9535")
    }
}