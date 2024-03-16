package com.example.skywatch.views.ui.map.views

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.skywatch.Constants
import com.example.skywatch.databinding.FragmentGoogleMapsBinding
import com.example.skywatch.helpers.SettingSharedPreferences
import com.example.skywatch.helpers.getAddressEnglish
import com.example.skywatch.helpers.getMarkerAddress
import com.example.skywatch.local.LocalDataSource
import com.example.skywatch.local.SkyWatchDatabase
import com.example.skywatch.models.FavoritePojo
import com.example.skywatch.models.LocationLatLngPojo
import com.example.skywatch.models.repos.WeatherRepo
import com.example.skywatch.remote.RemoteDataSource
import com.example.skywatch.remote.RetrofitHelper
import com.example.skywatch.views.ui.map.viewModel.MapViewModel
import com.example.skywatch.views.ui.map.viewModel.MapViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() ,OnMapReadyCallback{
    private lateinit var binding :FragmentGoogleMapsBinding
    private lateinit var map: GoogleMap
    private var selectedLatLng = LatLng(-34.0, 151.0)
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapViewModelFactory: MapViewModelFactory
    private val settingPref by lazy {
        SettingSharedPreferences.getInstance(requireActivity().application)
    }
//    lateinit var mapView: MapView
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapsBinding.inflate(inflater,container,false)
        mapViewModelFactory = MapViewModelFactory(
            WeatherRepo.getInstance(
                RemoteDataSource.getInstance(RetrofitHelper.service),
                LocalDataSource.getInstance(SkyWatchDatabase.getInstance(requireActivity()).SkyWatchDao())))
        mapViewModel = ViewModelProvider(this,mapViewModelFactory)[MapViewModel::class.java]
        return binding.root//inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        val type = MapsFragmentArgs.fromBundle(requireArguments()).type
        binding.getLocationBtn.setOnClickListener {
            var address = getAddressEnglish(requireActivity(),selectedLatLng.latitude,selectedLatLng.longitude)
            when(type)
            {
                Constants.FavNavType->{

                    mapViewModel.insertFavLocation(FavoritePojo(address,selectedLatLng.latitude,selectedLatLng.longitude))
                    val action : NavDirections =MapsFragmentDirections.actionMapsFragmentToNavFav()
                    Navigation.findNavController(it).navigate(action)
                }
                Constants.AlertNavType->{
                    val alertData=LocationLatLngPojo(address,selectedLatLng.latitude,selectedLatLng.longitude)
                    val action = MapsFragmentDirections.actionMapsFragmentToNavAlert()
                    action.setAlertLatLng(alertData)
                    Navigation.findNavController(it).navigate(action)
                }
                Constants.HomeNavType->{
                    settingPref.setMapPref(LatLng(selectedLatLng.latitude,selectedLatLng.longitude))
                    val action = MapsFragmentDirections.actionMapsFragmentToNavHome()
                    //action.setLatLng(null)
                    Navigation.findNavController(it).navigate(action)
                }
                Constants.SettingNavType->{
                    settingPref.setMapPref(LatLng(selectedLatLng.latitude,selectedLatLng.longitude))
                    settingPref.setLocationPref(SettingSharedPreferences.MAP)
                    val action = MapsFragmentDirections.actionMapsFragmentToSettingsFragment2()
                    Navigation.findNavController(it).navigate(action)
                }
            }
            Toast.makeText(requireActivity(), "${selectedLatLng.latitude} ${selectedLatLng.longitude}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val currentLocation = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        binding.mapView.getMapAsync(){
            it.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
        map.setOnMapClickListener { latLng ->
            // Move the camera to the clicked position
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

            // Clear existing markers (if any)
            googleMap.clear()
            selectedLatLng=latLng
            // Add a marker at the clicked position
            googleMap.addMarker(MarkerOptions().position(latLng).title(getMarkerAddress(requireActivity(),map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)).snippet("Marker Snippet"))
        }
        map.setOnCameraMoveListener {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                LatLng(
                    map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
                )
            ))

            // Clear existing markers (if any)
            googleMap.clear()
            selectedLatLng =LatLng(
                map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
            )
            // Add a marker at the clicked position
            googleMap.addMarker(MarkerOptions().position(
                LatLng(
                    map.cameraPosition.target.latitude, map.cameraPosition.target.longitude
                )
                    //getAddressEnglish(requireActivity(),map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
            ).title("location").snippet("Marker Snippet"))
        }
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}