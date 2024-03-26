package es.proyectoMad

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import es.upm.btb.helloworldkt.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.Distance
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import es.proyectoMad.persistence.retrofit.OverpassApiService
import es.proyectoMad.persistence.retrofit.data.OverpassResponse
import es.proyectoMad.persistence.room.AppDatabase
import es.proyectoMad.persistence.room.BarEntity
import es.proyectoMad.persistence.room.IBarDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class OpenStreetMapActivity : AppCompatActivity() {
    private val TAG = "btaOpenStreetMapActivity"
    private lateinit var map: MapView
    lateinit var database: AppDatabase
    private var maxDistance = 5000
    private lateinit var startPoint: GeoPoint
    private lateinit var currentLocationMarker: Marker
    private lateinit var barDao: IBarDao // Bar DAO instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_street_map)

        Log.d(TAG, "onCreate: The activity is being created.")

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        // Display open street map
        Configuration.getInstance()
            .load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(18.0)

        if (location != null) {
            Toast.makeText(
                this,
                "There is a location",
                Toast.LENGTH_SHORT
            ).show()
            startPoint = GeoPoint(location.latitude, location.longitude)
            map.controller.setCenter(startPoint)
            addMarker(startPoint, "My current location", R.drawable.iconoubicacion)
            fetchNearbyRestaurants(startPoint,maxDistance)
        } else {
            Toast.makeText(this, "NO LOCATION SET", Toast.LENGTH_SHORT)
                .show()
        }

        val setMaxDistanceButton = findViewById<Button>(R.id.setMaxDistanceButton)
        setMaxDistanceButton.setOnClickListener {
            // Abrir el diálogo para ingresar la distancia máxima
            inputMaxDistance()
        }
        // Initialize the DAO for bar entity
        barDao = AppDatabase.getInstance(this).barDao()
    }

    private fun addMarker(point: GeoPoint, title: String, iconResId: Int): Marker {
        val marker = Marker(map)
        marker.position = point
        marker.icon = ContextCompat.getDrawable(this, iconResId)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        map.overlays.add(marker)
        map.invalidate() // Reload map
        return marker
    }

    // Método para que el usuario ingrese la distancia máxima
    private fun inputMaxDistance() {
        val dialogBuilder = AlertDialog.Builder(this)
        val editText = EditText(this)

        dialogBuilder.setTitle("Enter Max Distance (in meters)")
        dialogBuilder.setView(editText)

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            val input = editText.text.toString()
            val newMaxDistance = if (input.isNotBlank()) {
                input.toInt()
            } else {
                5000 // Valor predeterminado si el usuario no ingresa nada
            }

            Log.d(TAG, "newMaxDistance = $newMaxDistance")
            dialog.dismiss()
            // Llamar a la función para buscar restaurantes con la nueva distancia máxima
            fetchNearbyRestaurants(startPoint, newMaxDistance)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun fetchNearbyRestaurants(point: GeoPoint, maxDistance: Int) {
        Log.d(TAG, "Estamos en la funcion fetchNearbyRestaurants")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OverpassApiService::class.java)

        val dataQuery =
            "[out:json];(node[\"amenity\"=\"restaurant\"](around:$maxDistance,${point.latitude},${point.longitude}););out;"
        val call = service.fetchNearbyBars(dataQuery)

        call.enqueue(object : Callback<OverpassResponse> {
            override fun onResponse(
                call: Call<OverpassResponse>,
                response: Response<OverpassResponse>
            ) {
                if (response.isSuccessful) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            // Eliminar todos los marcadores existentes antes de agregar nuevos
                            map.overlays.clear()
                            // Vuelve a agregar el marcador de ubicación actual
                            currentLocationMarker = addMarker(startPoint, "My current location", R.drawable.iconoubicacion)

                            // Procesar la respuesta de la API y guardar los datos en la base de datos
                            response.body()?.elements?.forEach { element ->
                                val restaurantName = element.tags?.get("name") ?: "Nombre no disponible"
                                val cuisine = element.tags?.get("cuisine")
                                val openingHours = element.tags?.get("opening_hours")
                                val delivery = element.tags?.get("delivery")?.toBoolean() ?: false
                                val outdoorSeating = element.tags?.get("outdoor_seating")?.toBoolean() ?: false
                                val reservation = element.tags?.get("reservation")?.toBoolean() ?: false
                                val address = element.tags?.get("addr:full")
                                val wheelchairAccessible = element.tags?.get("wheelchair")?.toBoolean() ?: false
                                val startDate = element.tags?.get("start_date")
                                val internetAccess = element.tags?.get("internet_access")?.toBoolean() ?: false
                                val smokingAllowed = element.tags?.get("smoking")?.toBoolean() ?: false
                                val image = element.tags?.get("image")
                                val websiteMenu = element.tags?.get("website:menu")
                                val website = element.tags?.get("website")
                                val phoneNumber = element.tags?.get("phone")
                                val michelinStars = element.tags?.get("stars")?.toIntOrNull()

                                val bar = BarEntity(
                                    name = restaurantName,
                                    cuisine = cuisine,
                                    openingHours = openingHours,
                                    takeaway = false, // No se proporciona en la respuesta de la API
                                    delivery = delivery,
                                    outdoorSeating = outdoorSeating,
                                    reservation = reservation,
                                    address = address,
                                    wheelchairAccessible = wheelchairAccessible,
                                    vegetarianFriendly = false, // No se proporciona en la respuesta de la API
                                    veganFriendly = false, // No se proporciona en la respuesta de la API
                                    startDate = startDate,
                                    internetAccess = internetAccess,
                                    smokingAllowed = smokingAllowed,
                                    image = image,
                                    websiteMenu = websiteMenu,
                                    website = website,
                                    phoneNumber = phoneNumber,
                                    michelinStars = michelinStars,
                                    kitchenOpeningHours = null // No se proporciona en la respuesta de la API
                                )

                                // Insertar en la base de datos
                                barDao.insertBar(bar)

                                // Agregar marcador en el mapa
                                addMarker(GeoPoint(element.lat, element.lon), restaurantName, R.drawable.bares1)
                            }

                            map.invalidate() // Actualizar el mapa
                            Log.d(TAG, "Se actualiza el mapa")
                        }
                    }
                } else {
                    Log.d(TAG, "La petición no fue exitosa")
                }
            }

            override fun onFailure(call: Call<OverpassResponse>, t: Throwable) {
                Log.d(TAG, "FALLO: ${t.message}")
            }
        })
    }


    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}

