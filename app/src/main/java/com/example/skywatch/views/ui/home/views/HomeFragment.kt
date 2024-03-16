package com.example.skywatch.views.ui.home.views

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
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.Constants
import com.example.skywatch.R
import com.example.skywatch.databinding.FragmentHomeBinding
import com.example.skywatch.databinding.InitialSetupSettingDialogBinding
import com.example.skywatch.helpers.NetworkConnectivity
import com.example.skywatch.helpers.SettingSharedPreferences
import com.example.skywatch.helpers.getACompleteDateFormat
import com.example.skywatch.helpers.getAddressEnglish
import com.example.skywatch.helpers.getWeatherImg
import com.example.skywatch.helpers.setTemp
import com.example.skywatch.helpers.setWindSpeed
import com.example.skywatch.local.LocalDataSource
import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.location.SkyWatchLocationManager
import com.example.skywatch.models.DailyItem
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.HomeStatus
import com.example.skywatch.remote.RemoteDataSource
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.ui.home.viewModel.HomeViewModel
import com.example.skywatch.views.ui.home.viewModel.HomeViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import java.util.TimeZone
import kotlin.math.log

private const val My_LOCATION_PERMISSION_ID = 5005
private var isLocationReceived = false // Add this variable

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding

    private val binding get() = _binding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var fusedClient : FusedLocationProviderClient
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var dailyLayoutManager:LinearLayoutManager
    private lateinit var hourlyLayoutManager:LinearLayoutManager
    lateinit var alertDialog: AlertDialog
    private val settingPref by lazy {
        SettingSharedPreferences.getInstance(requireActivity().application)
    }
    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(
                RemoteDataSource.getInstance(RetrofitHelper.service),
                LocalDataSource.getInstance(SkyWatchDatabase.getInstance(requireActivity()).SkyWatchDao())
                ),
            SkyWatchLocationManager.getInstance(requireActivity().application)
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyLayoutManager = LinearLayoutManager(requireActivity())
        dailyLayoutManager.orientation=RecyclerView.HORIZONTAL
        val dailyClick:(DailyItem)->Unit=
            {
                setUpCardDailyData(it)
            }
        dailyAdapter = DailyAdapter(TimeZone.getDefault(),dailyClick,binding.dailyRecyclerView)
        hourlyLayoutManager = LinearLayoutManager(requireActivity())
        hourlyLayoutManager.orientation=RecyclerView.HORIZONTAL
        hourlyAdapter = HourlyAdapter(TimeZone.getDefault())

        binding.dailyRecyclerView.adapter = dailyAdapter
        binding.dailyRecyclerView.layoutManager=dailyLayoutManager

        binding.hourlyRecyclerView.adapter = hourlyAdapter
        binding.hourlyRecyclerView.layoutManager = hourlyLayoutManager
        binding.swipeRefreshLayout.setOnRefreshListener {
            findNavController().navigate(R.id.nav_home)
            onResume()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermission()&&settingPref.getLocationPref() != SettingSharedPreferences.MAP)
        {
            settingPref.setLocationPref("null")
        }
        //setup Views
        binding.homeViewsGroup.visibility = View.GONE
        binding.progressHomeCircular.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.errorCard.visibility=View.GONE

        /////////////////////
        binding.reTryLoadWeatherBtn.setOnClickListener {
            isLocationReceived = false
//            getLocation()
        }
        ////////////
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showInitialSetupDialog() {
        Log.i("TAG", "onResume location Pref : ${settingPref.getLocationPref()} ")
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
        val initialSetupSettingDialogBinding: InitialSetupSettingDialogBinding =
            InitialSetupSettingDialogBinding.inflate(LayoutInflater.from(requireActivity()), null, false)
        alertDialog = materialAlertDialogBuilder.setView(initialSetupSettingDialogBinding.root)
            .setTitle(getString(R.string.intial_setup)).setIcon(R.drawable.baseline_settings_24)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(false).show()

        initialSetupSettingDialogBinding.buttonSave.setOnClickListener {
            if (initialSetupSettingDialogBinding.radioMap.isChecked) {
                settingPref.setLocationPref(SettingSharedPreferences.MAP)
                if (networkConnectivity.isOnline()) {
                    val action = HomeFragmentDirections.actionNavHomeToMapsFragment(Constants.HomeNavType)
                    Navigation.findNavController(requireView()).navigate(action)
                } else {
                    Snackbar.make(requireView(), getString(R.string.check_your_connection), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.dismiss)) {
                        }.show()                }

            } else {
                settingPref.setLocationPref(SettingSharedPreferences.GPS)
                getLocation()
//                onResume()
            }
            alertDialog.dismiss()

        }

        initialSetupSettingDialogBinding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.startShimmer()

        val args =HomeFragmentArgs.fromBundle(requireArguments())
        val latLang = args.latLng
        if (latLang!=null)
        {
            homeViewModel.getWeather(latLang.lat.toString(),latLang.lng.toString())
            getWeatherData(latLang.lat,latLang.lng)
            Log.i("TAG", "onResume: =============")
            binding.reTryLoadWeatherBtn.setOnClickListener {
                getWeatherData(latLang.lat,latLang.lng)
            }
        }
        else
        {
            if (NetworkConnectivity.getInstance(requireActivity().application).isOnline())
            {
                /*isLocationReceived = false
                getLocation()*/
                Log.i("TAG", "onResume location Pref : ${settingPref.getLocationPref()} ")

                when (settingPref.getLocationPref()) {

                    SettingSharedPreferences.MAP -> {
                        val mapLatLng = settingPref.getMapPref()
                        homeViewModel.getWeather(mapLatLng.latitude.toString(),mapLatLng.longitude.toString())
                        getWeatherData(mapLatLng.latitude,mapLatLng.longitude)
                    }
                    SettingSharedPreferences.GPS -> {
                        isLocationReceived = false
                        getLocation()
                    }
                    else -> {
                        showInitialSetupDialog()
                    }
                }
            }else
            {
                val pojo = loadWeatherPojoFromFile()
                if (pojo != null)
                {
                    setUpUI(pojo,pojo.lat?:0.0,pojo.lon?:0.0)
                    binding.homeViewsGroup.visibility = View.VISIBLE
                    binding.progressHomeCircular.visibility=View.GONE
                    binding.shimmerLayout.visibility=View.GONE
                    binding.errorCard.visibility=View.GONE
                }
            }

        }
    }
    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }
    override fun onDestroyView() {
        binding.shimmerLayout.stopShimmer()
        super.onDestroyView()
        //_binding
    }

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
            requireActivity() ,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            My_LOCATION_PERMISSION_ID
        )
    }
    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireActivity(),
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
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.myLooper()
        )
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val locationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            if (!isLocationReceived) {
                isLocationReceived = true
                val lastLocation : Location? = p0.lastLocation
                Log.i(Constants.TAG, "onLocationResult: ${lastLocation?.latitude.toString()}  ${lastLocation?.longitude.toString()}")
                homeViewModel.getWeather(lastLocation?.latitude.toString(),lastLocation?.longitude.toString())
                getWeatherData(lastLocation?.latitude?:0.0,lastLocation?.longitude?:0.0)
            }
        }
    }

    fun getWeatherData(lat:Double,lng:Double)=lifecycleScope.launch(Dispatchers.Main){
        homeViewModel.weatherData.collectLatest {
            when(it)
            {
                is HomeStatus.Loading->{
                    binding.homeViewsGroup.visibility = View.GONE
                    binding.progressHomeCircular.visibility=View.VISIBLE
                    binding.shimmerLayout.visibility=View.VISIBLE
                    binding.errorCard.visibility=View.GONE
                }
                is HomeStatus.Success->{
                    setUpUI(it.weatherPojo,lat,lng)
                    binding.homeViewsGroup.visibility = View.VISIBLE
                    binding.progressHomeCircular.visibility=View.GONE
                    binding.shimmerLayout.visibility=View.GONE
                    binding.errorCard.visibility=View.GONE
                }
                is HomeStatus.Failure->{
                    binding.errorCard.visibility=View.VISIBLE
                    binding.homeViewsGroup.visibility = View.GONE
                    binding.progressHomeCircular.visibility=View.GONE
                    binding.shimmerLayout.visibility=View.GONE
                    Toast.makeText(requireActivity(), it.errMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun setUpUI(it:WeatherPojo,lat:Double,lng:Double){
        saveResponseToFile(it)
        hourlyAdapter.submitList(it.hourly)
        dailyAdapter.submitList(it.daily)

        binding.dateOfDay.text = getACompleteDateFormat(
            it.current?.dt?.times(1000L) ?: -1,
            TimeZone.getTimeZone(it.timezone)
        )

        binding.locationAddressTV.text = if (NetworkConnectivity.getInstance(requireActivity().application).isOnline())
        {
            getAddressEnglish(
            requireActivity(),
            lat,
            lng
        )
        }
        else{
            getString(R.string.check_your_connection)
        }

        binding.currentImg.setImageResource(getWeatherImg(it.current?.weather?.get(0)?.icon ?:"null"))
//        binding.lottieCurrent.setAnimation(getWeatherLottie(it.current?.weather?.get(0)?.icon ?:"null"))

        binding.tempTV.setTemp(

            it.current?.temp?.toInt()?:-1, context = requireActivity().application
        )

        /*binding.txtViewPressure.text = buildString {
            append(it.current?.pressure.toString())
            append(getString(R.string.hpa))
        }*/

        /*binding.txtViewUV.text = it.current?.uvi.toString()*/
        /*binding.txtViewVisibility.text = buildString {
            append(it.current?.visibility.toString())
            append(getString(R.string.m))
        }*/
        /*binding.txtViewCloud.text = buildString {
            append(it.current?.clouds.toString())
            append(" %")
        }*/
       /* binding.txtViewHumidity.text = buildString {
            append(it.current?.humidity.toString())
            append(" %")
        }*/
/*
        binding.txtViewWind.setWindSpeed(
            it.current?.windSpeed?:-1.0, requireActivity().application)*/
        it.daily?.get(0)?.let { it1 -> setUpCardDailyData(it1) }
    }

    fun setUpCardDailyData(it: DailyItem)
    {
        binding.txtViewPressure.text = buildString {
            append(it.pressure.toString())
            append(getString(R.string.hpa))
        }
        binding.txtViewUV.text = it.uvi.toString()
        /*binding.txtViewVisibility.text = buildString {
            append(it.current?.visibility.toString())

            append(getString(R.string.m))
        }*/
        binding.txtViewCloud.text = buildString {
            append(it.clouds.toString())
            append(" %")
        }
        binding.txtViewHumidity.text = buildString {
            append(it.humidity.toString())
            append(" %")
        }
        binding.txtViewWind.setWindSpeed(
            it.windSpeed?:-1.0, requireActivity().application)
    }
    private fun startEnableLocation()
    {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
    private fun saveResponseToFile(response: WeatherPojo) {
        val context = requireActivity().applicationContext
        val filename = Constants.FileSavePojoName

        // Clear the file before saving the response
        context.deleteFile(filename)

        val jsonString = Gson().toJson(response)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }
    private fun loadWeatherPojoFromFile(): WeatherPojo? {
        val filename = Constants.FileSavePojoName
        return try {
            val inputStream = requireActivity().openFileInput(filename)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(jsonString, WeatherPojo::class.java)
        } catch (e: FileNotFoundException) {
            null
        } catch (e: IOException) {
            null
        }
    }

}