package es.helloworldkt

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.helloworldkt.persistence.retrofit.ISpoonacular
import es.helloworldkt.persistence.retrofit.data.Restaurant
import es.helloworldkt.persistence.room.AppDatabase
import es.helloworldkt.persistence.room.LocationEntity
import es.upm.btb.helloworldkt.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private var latestLocation: Location? = null
    private val locationPermissionCode = 2
    private lateinit var database: AppDatabase
    private lateinit var spoonacularService: ISpoonacular

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: La actividad está siendo creada.")
        println("¡Hola mundo para probar la salida estándar System.out!")

        // BottomNavigationMenu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            val currentActivity = this::class.java.simpleName
            when (item.itemId) {
                R.id.navigation_home -> if (currentActivity != MainActivity::class.java.simpleName) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.navigation_map -> if (currentActivity != OpenStreetMapActivity::class.java.simpleName) {
                    if (latestLocation != null) {
                        val intent = Intent(this, OpenStreetMapActivity::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable("location", latestLocation)
                        intent.putExtra("locationBundle", bundle)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "Ubicación aún no establecida.")
                        startActivity(Intent(this, OpenStreetMapActivity::class.java))
                    }
                    true
                }
                R.id.navigation_list -> if (currentActivity != SecondActivity::class.java.simpleName) {
                    startActivity(Intent(this, SecondActivity::class.java))
                }
            }
            true
        }

        // Configurar la barra de herramientas
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Preferencias compartidas. Comprueba si el identificador de usuario ya está guardado
        val userIdentifier = getUserIdentifier()
        if (userIdentifier == null) {
            askForUserIdentifier()
        } else {
            Toast.makeText(this, "ID de usuario: $userIdentifier", Toast.LENGTH_LONG).show()
        }

        // Inicialización del gestor de ubicación y permisos
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionCode
            )
        } else {
            // La ubicación se actualiza cada 5000 milisegundos (o 5 segundos) y/o si el dispositivo se mueve más de 5 metros,
            // lo que ocurra primero
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }

        // Inicialización de la base de datos Room
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "coordinates").build()

        // Inicialización de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/") // URL base para la API de Spoonacular
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        spoonacularService = retrofit.create(ISpoonacular::class.java)

        // Ejemplo de cómo llamar a la función
        // Reemplaza API_KEY con tu clave API real
        requestRestaurantData(40.7128, -74.0060, "ffad85840ab14db5928abe6a5df4e709")
    }

    override fun onLocationChanged(location: Location) {
        latestLocation = location
        val textView: TextView = findViewById(R.id.mainTextView)
        textView.text = "\uD83D\uDCCD Latitud: [${location.latitude}], Longitud: [${location.longitude}], ID de usuario: [${getUserIdentifier()}]"
        Toast.makeText(this, "¡Actualización de coordenadas! [${location.latitude}][${location.longitude}]", Toast.LENGTH_LONG).show()

        // Guardar las coordenadas en la base de datos Room
        val newLocation = LocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            database.locationDao().insertLocation(newLocation)
        }
    }

    // Otros métodos...

    private fun requestRestaurantData(latitude: Double, longitude: Double, apiKey: String) {
        val restaurantDataCall = spoonacularService.getRestaurants(
            latitude = latitude,
            longitude = longitude,
            radius = 5000, // Radio de búsqueda en metros
            apiKey = apiKey
        )

        restaurantDataCall.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    response.body()?.let { restaurants ->
                        // Manejar la lista de restaurantes y mostrarlos en el mapa
                        // Ejemplo: mostrarRestaurantesEnMapa(restaurants)
                    }
                } else {
                    // Manejar errores de respuesta
                    Log.e("SolicitudRestaurante", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                // Manejar errores de conexión
                Log.e("SolicitudRestaurante", "Error: ${t.message}", t)
            }
        })
    }

    private fun getUserIdentifier(): String? {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userIdentifier", null)
    }

    private fun askForUserIdentifier() {
        val dialogBuilder = AlertDialog.Builder(this)
        val editText = EditText(this)

        // Establecer el mensaje y el título del diálogo
        dialogBuilder.setMessage("Por favor, introduce tu identificador de usuario:")
            .setTitle("Identificación de Usuario")

        // Agregar un campo de entrada al diálogo
        dialogBuilder.setView(editText)

        // Agregar botones al diálogo
        dialogBuilder.setPositiveButton("Guardar") { dialog, id ->
            // Guardar el identificador de usuario en SharedPreferences
            val input = editText.text.toString()
            saveUserIdentifier(input)
            Toast.makeText(this, "ID de usuario guardado: $input", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, id ->
            // Usuario cancela la operación, no se guarda el identificador
            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show()
        }

        // Crear y mostrar el diálogo
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun saveUserIdentifier(identifier: String) {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userIdentifier", identifier)
        editor.apply()
    }
}
