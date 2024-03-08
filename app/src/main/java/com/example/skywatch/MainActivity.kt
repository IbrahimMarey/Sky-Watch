package com.example.skywatch

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.skywatch.databinding.ActivityMainBinding
import com.example.skywatch.databinding.InitialSetupSettingDialogBinding
import com.example.skywatch.helpers.NetworkConnectivity
import com.example.skywatch.helpers.SettingSharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val My_LOCATION_PERMISSION_ID = 5005

class MainActivity : AppCompatActivity() {
    private lateinit var fusedClient : FusedLocationProviderClient
    private var isLocationReceived = false
//    lateinit var homeViewModel: HomeViewModel
//    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var binding: ActivityMainBinding
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val settingPref by lazy {
        SettingSharedPreferences.getInstance(application)
    }/*
    private val mainViewModel: MainViewModel by lazy {
        factory = MainViewModel.Factory(
            _repo = WeatherDataRepo.getInstance(
                APIClient,
                DefaultLocalDataSource.getInstance(getDatabase(applicationContext).weatherDao)
            ), _loc = WeatherLocationManager.getInstance(application)
        )
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }*/
    private var isConnected = true
    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        homeViewModelFactory = HomeViewModelFactory(WeatherRepo.getInstance(RetrofitHelper))
//        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)
//        homeViewModel.getWeather("30.1771","31.9535")


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
//        getLocation()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

/*
    private fun checkPermissionAndGetLoc() {
        if (checkPermission()) {
            Log.d("TAG", "onResume: ")
//            getLastLocation()
        } else {
            ActivityCompat.requestPermissions(
                this, locationPermissions, My_LOCATION_PERMISSION_ID
            )
        }
    }

    lateinit var alertDialog: AlertDialog
    private fun showInitialSetupDialog() {

        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        val initialSetupSettingDialogBinding: InitialSetupSettingDialogBinding =
            InitialSetupSettingDialogBinding.inflate(LayoutInflater.from(this), null, false)


        alertDialog = materialAlertDialogBuilder.setView(initialSetupSettingDialogBinding.root)
            .setTitle(getString(R.string.intial_setup)).setIcon(R.drawable.baseline_settings_24)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, theme
                )
            ).setCancelable(false).show()

        initialSetupSettingDialogBinding.buttonSave.setOnClickListener {
            if (initialSetupSettingDialogBinding.radioMap.isChecked) {
                settingPref.setLocationPref(SettingSharedPreferences.MAP)
                if (networkConnectivity.isOnline()) {
//                    openMapToSetLocation()
                } else {
                    errorWarningForNoNetworkWithUsingMap()
                }

            } else {
                settingPref.setLocationPref(SettingSharedPreferences.GPS)
                checkPermissionAndGetLoc()
            }
            alertDialog.dismiss()

        }

        initialSetupSettingDialogBinding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    */
    /*private fun openMapToSetLocation() {
        with(Intent(this, MapsActivity::class.java)) {
            putExtra(NAVIGATE_TO_MAP, SET_LOCATION_AS_MAIN_LOCATION)
            startActivity(this)
        }
    }*/
    /*


    */
    /*private fun getLastLocation() {
        Log.d(TAG, "getLastLocation: ")
        if (mainViewModel.isLocationEnabled()) {
            mainViewModel.requestLocationUpdateByGPS()
        } else {
            checkIsLocationEnabledDialog()
        }
    }*/
    /*


    */
    /*private fun checkIsLocationEnabledDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.location_request))
            .setCancelable(false).setMessage(getString(R.string.please_enable_loc))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                if (!mainViewModel.isLocationEnabled()) {
                    goToEnableTheLocation()
                }
            }.setNegativeButton(
                getString(R.string.no)
            ) { _, _ ->
                errorWarningForNotEnablingLocation()
                showSnackBarAskingHimToEnable()
            }.show()
    }*//*
    private fun goToEnableTheLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    */
    /*private fun showSnackBarAskingHimToEnable() {
        val snackBar = Snackbar.make(
            binding.root, getString(R.string.please_enable_loc), Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction(getString(R.string.enable)) {
            if (!mainViewModel.isLocationEnabled()) {
                goToEnableTheLocation()
            }
            snackBar.dismiss()
        }.setBackgroundTint(getColor(R.color.textColor)).setTextColor(getColor(R.color.background))

        snackBar.setActionTextColor(getColor(R.color.background))
        snackBar.anchorView = binding.navView
        snackBar.show()
    }*/
    /*

    private fun errorWarningForNotEnablingLocation() {
        AlertDialog.Builder(this).setTitle(getString(R.string.warning)).setCancelable(false)
            .setMessage(
                getString(R.string.Unfortunately_the_location_is_disabled)
            ).setPositiveButton(android.R.string.ok) { _, _ -> }.show()
    }

    private fun errorWarningForNoNetworkWithUsingMap() {
        AlertDialog.Builder(this).setTitle(getString(R.string.warning)).setCancelable(false)
            .setMessage(
                getString(R.string.Unfortunately_no_network_connection_loc_from_map)
            ).setPositiveButton(android.R.string.ok) { _, _ -> }
            .setNeutralButton(R.string.gps) { _, _ ->
                settingPref.setLocationPref(SettingSharedPreferences.GPS)
                checkPermissionAndGetLoc()
            }.show()

    }


    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return false
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation()
            }
        }
    }*/


   /* @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            if (lastLocation != null)
            {

            }
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
    }*/

}