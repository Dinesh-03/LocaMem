package com.ktt.locamem.screen.map

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ktt.locamem.R
import com.ktt.locamem.databinding.ActivityMapsBinding
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var latLng: LatLng
    private var timeStamp by Delegates.notNull<Long>()
    private lateinit var userName: String
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName = SharedPreferences.getString(this, Constants.USER_PREFERENCE, Constants.USER_NAME)
        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        binding.back.setOnClickListener {
            finish()
        }

        binding.play.setOnClickListener {
            mapsViewModel.getLocations(userName)
        }

        val args = intent.extras!!
        latLng = LatLng(
            args.getString(Constants.LATITUDE, "0.0").toDouble(),
            args.getString(Constants.LONGITUDE, "0.0").toDouble()
        )
        timeStamp = args.getLong(Constants.TIMESTAMP)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(latLng).title("Marker : ${getDateTime(timeStamp)}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18F))

        mapsViewModel.locations.observe(this) {
            mMap.clear()
            val latLngBuilder = LatLngBounds.Builder()
            it.forEach { locationData ->
                run {
                    val latLng =
                        LatLng(locationData.latitude.toDouble(), locationData.longitude.toDouble())
                    latLngBuilder.include(latLng)
                    val formattedDateTime = getDateTime(locationData.timeStamp)
                    Log.d("Maps", "onMapReady: $formattedDateTime")
                    mMap.addMarker(
                        MarkerOptions().position(latLng).title("Marker : $formattedDateTime")
                    )
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            }
            val bounds = latLngBuilder.build()
            val width = getResources().displayMetrics.widthPixels
            val height = getResources().displayMetrics.heightPixels
            val padding = (width * 0.10).toInt()
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding))
        }
    }

    private fun getDateTime(timeStamp: Long): String {
        val instant = Instant.ofEpochMilli(timeStamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"))
    }
}