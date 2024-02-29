package com.example.proyectomadmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class ThirdActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")
        val buttonPrevious: Button = findViewById(R.id.button4)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
