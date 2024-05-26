package com.example.geostatapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geostatapplication.R
import com.example.geostatapplication.api.config.ApiConfig
import com.example.geostatapplication.api.response.FeaturesItem
import com.example.geostatapplication.api.response.GeoStatResponse
import com.example.geostatapplication.databinding.ActivityMainBinding
import com.example.geostatapplication.ui.itemDesa.ItemDaftarDesaAdapter
import com.example.geostatapplication.ui.maps.MapsActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var desaAdapter: ItemDaftarDesaAdapter
    private lateinit var fullListDesa: List<FeaturesItem>

    companion object {
        private var TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setIndicatorBar()
        setRecyclerView()
        getVilages()
        setAction()
    }

    private fun setAction() {
        binding.edSearch.addTextChangedListener {
            val keyword = it.toString().trim()
            if (keyword.isNotBlank()) {
                desaAdapter.searchDesa(keyword)
            } else {
                desaAdapter.updateDataDesa(fullListDesa)
            }
        }
        binding.btnDropDown.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setIndicatorBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.indent_75)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun setRecyclerView() {
        desaAdapter = ItemDaftarDesaAdapter(
            this,
            emptyList()
        ) { desa ->

            val data = desa.properties
            val dataKordinat = desa.geometry
            val latitude = dataKordinat?.coordinates!![0][0][1]
            val longitude = dataKordinat.coordinates[0][0][0]
            val lokasi = data?.nmsls
            val provinsi = data?.nmprov
            val kabupaten = data?.nmkab
            val jumlahKk = data?.kk.toString()
            val namaDesa = data?.nmdesa
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("lokasi", lokasi)
            intent.putExtra("provinsi", provinsi)
            intent.putExtra("kabupaten", kabupaten)
            intent.putExtra("jumlahKk", jumlahKk)
            intent.putExtra("nama_desa", namaDesa)
            intent.putExtra("lat", latitude.toString())
            intent.putExtra("lon", longitude.toString())
            startActivity(intent)
        }
        binding.rvDaftarDesa.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = desaAdapter
        }
    }

    private fun getVilages() {
        showLoading(true)
        ApiConfig.instanceRetrofit.getVillages().enqueue(
            object : Callback<GeoStatResponse>{
                override fun onResponse(
                    call: Call<GeoStatResponse>,
                    response: Response<GeoStatResponse>
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        val desa = response.body()?.features
                        Log.d(TAG, "Tampilkan : ${desa?.toString()}")
                        fullListDesa = desa as List<FeaturesItem>
                        desaAdapter.updateDataDesa(fullListDesa)
                    } else {
                        Log.d(TAG, "onFailure: ${response.message()}")
                        showSnackbar("Gagal Mendapatkan Data")
                    }
                }

                override fun onFailure(call: Call<GeoStatResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                    showSnackbar("Gagal Mendapatkan Data")
                }

            }
        )
    }

    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun showLoading(isLoading: Boolean) {
        when(isLoading) {
            true -> {
                binding.shimmerView.isVisible = true
                binding.rvDaftarDesa.isGone = true
            }
            else -> {
                binding.shimmerView.isGone = true
                binding.rvDaftarDesa.isVisible = true
            }
        }
    }
}