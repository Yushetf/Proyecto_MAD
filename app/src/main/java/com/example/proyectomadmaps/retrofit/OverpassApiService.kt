package com.example.proyectomadmaps.retrofit

import com.example.proyectomadmaps.retrofit.data.OverpassResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassApiService {
    @GET("interpreter")
    fun fetchNearbyBars(
        @Query("data", encoded = true) data: String
    ): Call<OverpassResponse>
}