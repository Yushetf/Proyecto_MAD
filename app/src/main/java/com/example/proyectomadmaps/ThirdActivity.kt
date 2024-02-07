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

        Log.d(TAG, "onCreate: Estamos en la tercera actividad");

        val buttonGoToSecond: Button = findViewById(R.id.button4)
        buttonGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}