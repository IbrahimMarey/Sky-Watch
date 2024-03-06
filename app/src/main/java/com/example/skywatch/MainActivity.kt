package com.example.skywatch

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.skywatch.databinding.ActivityMainBinding
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.ui.home.viewModel.HomeViewModel
import com.example.skywatch.views.ui.home.viewModel.HomeViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
//    lateinit var homeViewModel: HomeViewModel
//    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(RetrofitHelper))
//        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)
//        homeViewModel.getWeather("30.1771","31.9535")


    }

    override fun onResume() {
        super.onResume()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}