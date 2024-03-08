package com.example.skywatch.views

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.skywatch.Constants
import com.example.skywatch.R
import com.example.skywatch.databinding.ActivitySkyWatchBinding
import com.example.skywatch.models.LocationLatLngPojo
import com.example.skywatch.views.ui.home.views.HomeFragmentArgs
import com.google.android.gms.location.FusedLocationProviderClient

private const val My_LOCATION_PERMISSION_ID = 5005

class SkyWatchActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySkyWatchBinding
    private lateinit var fusedClient : FusedLocationProviderClient
    private var isLocationReceived = false
    private lateinit var navController :NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySkyWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarSkyWatch.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_sky_watch)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fav, R.id.nav_alert
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sky_watch, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_sky_watch)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /////////////////////////////////////// Location


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.mapsFragment) {
                binding.appBarSkyWatch.cardBar.visibility =View.GONE
            } else {
                binding.appBarSkyWatch.cardBar.visibility =View.VISIBLE

            }
        }
//        getLocation()
    }
    /*
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun getLocation()
    {
        if (checkPermission())
        {
            if (isLocationEnabled())
            {
                requestNewLocationData()
            }else
            {
                startEnableLocation()

            }
        }else
        {
            requestPermission()
        }
    }
    private fun requestPermission()
    {
        ActivityCompat.requestPermissions(
            this ,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            My_LOCATION_PERMISSION_ID
        )
    }
    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID )
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getLocation()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(0)
        fusedClient = LocationServices.getFusedLocationProviderClient(this)

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.myLooper()
        )
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val locationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {

//            if (!isLocationReceived) {
                isLocationReceived = true
                val lastLocation : Location? = p0.lastLocation
                Log.i(Constants.TAG, "onLocationResult: ${lastLocation?.latitude.toString()}  ${lastLocation?.longitude.toString()}")
                *//*homeViewModel.getWeather(lastLocation?.latitude.toString(),lastLocation?.longitude.toString())
                lifecycleScope.launch(Dispatchers.Main){
                    homeViewModel.weatherData.collectLatest {
                        when(it)
                        {
                            is HomeStatus.Loading->{
                                binding.homeViewsGroup.visibility = View.GONE
                                binding.progressHomeCircular.visibility= View.VISIBLE
                                binding.shimmerLayout.visibility= View.VISIBLE
                                binding.errorCard.visibility= View.GONE
                            }
                            is HomeStatus.Success->{
                                setUpUI(it.weatherPojo,lastLocation)
                                binding.homeViewsGroup.visibility = View.VISIBLE
                                binding.progressHomeCircular.visibility= View.GONE
                                binding.shimmerLayout.visibility= View.GONE
                                binding.errorCard.visibility= View.GONE
                            }
                            is HomeStatus.Failure->{
                                binding.errorCard.visibility= View.VISIBLE
                                binding.homeViewsGroup.visibility = View.GONE
                                binding.progressHomeCircular.visibility= View.GONE
                                binding.shimmerLayout.visibility= View.GONE
                            }
                        }
                    }
                }*//*
//            }
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun startEnableLocation()
    {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
*/
}