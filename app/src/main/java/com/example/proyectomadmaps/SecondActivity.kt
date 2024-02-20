package com.example.proyectomadmaps

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.IOException

class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d(TAG, "onCreate: Estamos en la segunda actividad");

        val buttonGoToThird: Button = findViewById(R.id.button2)
        buttonGoToThird.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val buttonGoToMain: Button = findViewById(R.id.button3)
        buttonGoToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Display the file contents
        val tvFileContents: TextView = findViewById(R.id.tvFileContents)
        tvFileContents.text = readFileContents()

    }

    private fun readFileContents(): String {
        val fileName = "gps_coordinates.csv"
        return try {
            // Open the file from internal storage
            openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }
        } catch (e: IOException) {
            "Error reading file: ${e.message}"
        }
    }

}