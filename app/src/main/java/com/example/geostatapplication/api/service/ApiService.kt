package com.example.geostatapplication.api.service

import com.example.geostatapplication.api.response.GeoStatResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("villages")
    fun getVillages(): Call<GeoStatResponse>
}