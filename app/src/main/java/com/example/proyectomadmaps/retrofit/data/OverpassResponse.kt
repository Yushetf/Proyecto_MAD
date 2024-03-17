package com.example.proyectomadmaps.retrofit.data

import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("version") val version: Double,
    @SerializedName("generator") val generator: String,
    @SerializedName("osm3s") val osm3s: Osm3s,
    @SerializedName("elements") val elements: List<Element>
)

data class Osm3s(
    @SerializedName("timestamp_osm_base") val timestampOsmBase: String,
    @SerializedName("copyright") val copyright: String
)

data class Element(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: Long,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("tags") val tags: Map<String, String>?
)
