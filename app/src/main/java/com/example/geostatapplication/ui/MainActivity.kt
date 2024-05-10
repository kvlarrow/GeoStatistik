package com.example.geostatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geostatapplication.api.config.ApiConfig
import com.example.geostatapplication.api.response.FeaturesItem
import com.example.geostatapplication.api.response.GeoStatResponse
import com.example.geostatapplication.databinding.ActivityMainBinding
import com.example.geostatapplication.ui.itemDesa.ItemDaftarDesaAdapter
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
    }

    private fun setRecyclerView() {
        desaAdapter = ItemDaftarDesaAdapter(
            this, emptyList()
        ) { desa ->

        }
        binding.rvDaftarDesa.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = desaAdapter
        }
    }

    private fun getVilages() {
        ApiConfig.instanceRetrofit.getVillages().enqueue(
            object : Callback<GeoStatResponse>{
                override fun onResponse(
                    call: Call<GeoStatResponse>,
                    response: Response<GeoStatResponse>
                ) {
                    if (response.isSuccessful) {
                        val desa = response.body()?.features
                        Log.d(TAG, "Tampilkan : ${desa?.toString()}")
                        fullListDesa = (desa ?: emptyList()) as List<FeaturesItem>
                        desaAdapter.updateDataDesa(fullListDesa)
                    }
                }

                override fun onFailure(call: Call<GeoStatResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            }
        )
    }
}