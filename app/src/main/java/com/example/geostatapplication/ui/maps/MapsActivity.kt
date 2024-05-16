package com.example.geostatapplication.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.geostatapplication.R
import com.example.geostatapplication.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private var TAG = "MapsActivity"
        private var LAT = 0.0
        private var LON = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = 90
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        LAT = -1.260692
        LON = 116.76238

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getMyLocation()
        setAction()
    }

    private fun setAction() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun moveCameraToLocation(lat: Double, lon: Double) {
        val latlng = LatLng(lat, lon)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17f)
        mMap.animateCamera(cameraUpdate)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        moveCameraToLocation(LAT, LON)
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    Log.d(TAG, "lat = $lat\nlon = $lon")
                } ?: run {
                    Log.e(TAG, "Last location is null")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }
}