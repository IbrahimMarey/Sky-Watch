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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skywatch.Constants
import com.example.skywatch.R
import com.example.skywatch.databinding.FragmentHomeBinding
import com.example.skywatch.helpers.getACompleteDateFormat
import com.example.skywatch.helpers.getAddressEnglish
import com.example.skywatch.helpers.getWeatherLottie
import com.example.skywatch.helpers.setTemp
import com.example.skywatch.helpers.setWindSpeed
import com.example.skywatch.location.SkyWatchLocationManager
import com.example.skywatch.models.WeatherPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.models.status.HomeStatus
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.ui.home.viewModel.HomeViewModel
import com.example.skywatch.views.ui.home.viewModel.HomeViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.TimeZone

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
    //location
    /*private lateinit var requestPermission: ActivityResultLauncher<Array<String>?>
    private lateinit var locationPermissions: Array<String>*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepo.getInstance(RetrofitHelper),
            SkyWatchLocationManager.getInstance(requireActivity().application)
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*//setup Views
        binding.homeViewsGroup.visibility = View.GONE
        binding.progressHomeCircular.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.errorCard.visibility=View.GONE
        ////////////
        binding.reTryLoadWeatherBtn.setOnClickListener {
            isLocationReceived = false
            getLocation()
        }
        getLocation()*/
        dailyLayoutManager = LinearLayoutManager(requireActivity())
        dailyLayoutManager.orientation=RecyclerView.HORIZONTAL
        dailyAdapter = DailyAdapter(TimeZone.getDefault())

        hourlyLayoutManager = LinearLayoutManager(requireActivity())
        hourlyLayoutManager.orientation=RecyclerView.HORIZONTAL
        hourlyAdapter = HourlyAdapter(TimeZone.getDefault())

        binding.dailyRecyclerView.adapter = dailyAdapter
        binding.dailyRecyclerView.layoutManager=dailyLayoutManager

        binding.hourlyRecyclerView.adapter = hourlyAdapter
        binding.hourlyRecyclerView.layoutManager = hourlyLayoutManager

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        //setup Views
        binding.homeViewsGroup.visibility = View.GONE
        binding.progressHomeCircular.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.errorCard.visibility=View.GONE
        ////////////
        binding.reTryLoadWeatherBtn.setOnClickListener {
            isLocationReceived = false
            getLocation()
        }
        getLocation()
        /*binding.reTryLoadWeatherBtn.setOnClickListener {
            if (!checkPermission())
            {
                requestPermission.launch(locationPermissions)
            }else if (!homeViewModel.isLocationEnabled())
            {
                Log.i(Constants.TAG, "onStart: Location don't allwo")
            }else{

            }
        }*/
    }
    override fun onResume() {
        binding.shimmerLayout.startShimmer()
        super.onResume()
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
                lifecycleScope.launch(Dispatchers.Main){
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
                                setUpUI(it.weatherPojo,lastLocation)
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
                            }
                        }
                    }
                }
            }
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun setUpUI(it:WeatherPojo,lastLocation : Location?){
        hourlyAdapter.submitList(it.hourly)
        dailyAdapter.submitList(it.daily)

        binding.dateOfDay.text = getACompleteDateFormat(
            it.current?.dt?.times(1000L) ?: -1,
            TimeZone.getTimeZone(it.timezone)
        )

        binding.locationAddressTV.text = getAddressEnglish(
            requireActivity(),
            lastLocation?.latitude,
            lastLocation?.longitude
        )

        binding.lottieCurrent.setAnimation(getWeatherLottie(it.current?.weather?.get(0)?.icon ?:"null"))

        binding.tempTV.setTemp(

            it.current?.temp?.toInt()?:-1, context = requireActivity().application
        )

        binding.txtViewPressure.text = buildString {
            append(it.current?.pressure.toString())
            append(getString(R.string.hpa))
        }

        binding.txtViewUV.text = it.current?.uvi.toString()
        binding.txtViewVisibility.text = buildString {
            append(it.current?.visibility.toString())
            append(getString(R.string.m))
        }
        binding.txtViewCloud.text = buildString {
            append(it.current?.clouds.toString())
            append(" %")
        }
        binding.txtViewHumidity.text = buildString {
            append(it.current?.humidity.toString())
            append(" %")
        }

        binding.txtViewWind.setWindSpeed(
            it.current?.windSpeed?:-1.0, requireActivity().application)
    }
    private fun startEnableLocation()
    {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}