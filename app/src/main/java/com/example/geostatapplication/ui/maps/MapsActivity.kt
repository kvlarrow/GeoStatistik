package com.example.geostatapplication.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.geostatapplication.R
import com.example.geostatapplication.api.response.FeaturesItem
import com.example.geostatapplication.databinding.ActivityMapsBinding
import com.example.geostatapplication.helper.capitalizeWords
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var dataDesaSelection: FeaturesItem

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

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")
        val latD = lat!!.toDouble()
        val lonD = lon!!.toDouble()
        LAT = latD
        LON = lonD

        setIndicatorBar()
        getMyLocation()
        setAction()
        setView()

    }

    private fun setIndicatorBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.indent_75)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun setAction() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            showSnackbar("Setelah : $LAT, $LON}")
        }
        binding.btnNavigasi.setOnClickListener {
            navigateToLocation(LAT, LON)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {

        val lokasi = intent.getStringExtra("lokasi")
        val provinsi = intent.getStringExtra("provinsi")
        val kabupaten = intent.getStringExtra("kabupaten")
        val jumlahKk = intent.getStringExtra("jumlahKk")
        val namaDesa = intent.getStringExtra("nama_desa")

        binding.tvLokasiDesa.text = "Lokasi : $lokasi"
        binding.tvProvinsi.text = "Provinsi : $provinsi"
        binding.tvKabupaten.text = "Kabupaten : $kabupaten"
        binding.tvJumlahKepalaKeluarga.text = jumlahKk
        binding.tvNamaDesa.text = capitalizeWords(namaDesa!!)
    }

    private fun moveCameraToLocation() {
        val latlng = LatLng(LAT, LON)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17f)
        mMap.animateCamera(cameraUpdate)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        moveCameraToLocation()
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

    private fun navigateToLocation(desLat: Double, desLon: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$desLat,$desLon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            showSnackbar("Google Maps app is not installed.")
        }
    }

    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}