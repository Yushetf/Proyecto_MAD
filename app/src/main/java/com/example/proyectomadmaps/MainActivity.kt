package com.example.proyectomadmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: Estamos en actividad principal")

        val buttonGoToSecond: Button = findViewById(R.id.button)
        buttonGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionCode
            )
        } else {
            // The location is updated every 5000 milliseconds (or 5 seconds) and/or if the device moves more than 5 meters,
            // whichever happens first
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val textView: TextView = findViewById(R.id.mainTextView)
        textView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}
}
