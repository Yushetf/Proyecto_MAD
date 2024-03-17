package es.helloworldkt.persistence.retrofit

import es.helloworldkt.persistence.retrofit.data.Restaurant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ISpoonacular {
    @GET("restaurants")
    fun getRestaurants(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radius: Int,
        @Query("apiKey") apiKey: String
    ): Call<List<Restaurant>>
}
