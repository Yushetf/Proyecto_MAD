package es.helloworldkt.persistence.retrofit.data

import com.google.gson.annotations.SerializedName

data class Restaurant (
    @SerializedName("name") var name: String? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    // Otros campos relevantes para el restaurante
)
