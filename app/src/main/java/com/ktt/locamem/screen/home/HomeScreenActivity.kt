package com.ktt.locamem.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ktt.locamem.databinding.ActivityHomeScreenBinding
import com.ktt.locamem.model.LocationData
import com.ktt.locamem.screen.home.user_dialog.UserDialogFragment
import com.ktt.locamem.screen.map.MapsActivity
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var userName: String

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                updateMetrics(location)
            }
        }
    }

    private val locationItemListener = object : LocationAdapter.LocationItemListener {
        override fun onClickedLocation(location: LocationData) {
            Toast.makeText(
                this@HomeScreenActivity,
                "${location.latitude} ${location.longitude}",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this@HomeScreenActivity, MapsActivity::class.java)
            intent.putExtra(Constants.LATITUDE, location.latitude)
            intent.putExtra(Constants.LONGITUDE, location.longitude)
            intent.putExtra(Constants.TIMESTAMP, location.timeStamp)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        userName = SharedPreferences.getString(this, Constants.USER_PREFERENCE, Constants.USER_NAME)
        binding.userName.text = userName
        setUpLocation()
        homeViewModel.getLocations(userName)

        locationAdapter = LocationAdapter(this, listOf(), locationItemListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = locationAdapter

        homeViewModel.locations.observe(this) {
            locationAdapter.updateLocations(it.reversed())
        }

    }

    override fun onResume() {
        super.onResume()
        setUpLocation()
        homeViewModel.getLocations(userName)
    }

    @SuppressLint("MissingPermission")
    fun setUpLocation() {
        if (hasLocationPermission()) {
            if (isGPSEnabled()) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "GPS is disabled. Please enable it.", Toast.LENGTH_SHORT)
                    .show()
                openGPSSettings()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.LOCATION_REQUEST_CODE
        )
    }

    private fun openGPSSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, Constants.REQUEST_GPS_SETTINGS)
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.MINUTES.toMillis(3))
                .apply {
                    setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                    setWaitForAccurateLocation(true)
                }.build()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun updateMetrics(location: Location) {
        println("${location.latitude} ${location.longitude} ${location.time}")
        val time = LocalDateTime.ofInstant(
            Instant.ofEpochMilli((location.time)), TimeZone
                .getDefault().toZoneId()
        )
        println("time : $time")
        homeViewModel.addLocationData(
            userName,
            location.latitude.toString(),
            location.longitude.toString(),
            location.time
        )
    }

}