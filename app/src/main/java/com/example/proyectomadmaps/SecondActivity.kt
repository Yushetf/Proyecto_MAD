package com.example.proyectomadmaps

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.io.IOException
import android.widget.ArrayAdapter
import android.widget.ListView
import java.util.*


class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d(TAG, "onCreate: Estamos en la segunda actividad");

        val buttonGoToThird: Button = findViewById(R.id.button2)
        buttonGoToThird.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Inflate heading and add to ListView
        val listView: ListView = findViewById(R.id.lvCoordinates)
        val headerView = layoutInflater.inflate(R.layout.listview_header, listView, false)
        listView.addHeaderView(headerView, null, false)
        // Create adapter of coordiantes. See class below
        val adapter = CoordinatesAdapter(this, readFileContents())
        listView.adapter = adapter
    }
    private class CoordinatesAdapter(context: Context, private val coordinatesList: List<List<String>>) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item, coordinatesList) {
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item, parent, false)

            val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
            val latitudeTextView: TextView = view.findViewById(R.id.tvLatitude)
            val longitudeTextView: TextView = view.findViewById(R.id.tvLongitude)
            val item = coordinatesList[position]
            timestampTextView.text = formatTimestamp(item[0].toLong())
            latitudeTextView.text = formatCoordinate(item[1].toDouble())
            longitudeTextView.text = formatCoordinate(item[2].toDouble())

            view.setOnClickListener {
                val intent = Intent(context, ThirdActivity::class.java).apply {
                    putExtra("latitude", item[1])
                    putExtra("longitude", item[2])
                }
                context.startActivity(intent)
            }
            return view
        }

        private fun formatTimestamp(timestamp: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        private fun formatCoordinate(value: Double): String {
            return String.format("%.6f", value)
        }
    }
    private fun readFileContents(): List<List<String>> {
        val fileName = "gps_coordinates.csv"
        return try {
            openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.map { it.split(";").map(String::trim) }.toList()
            }
        } catch (e: IOException) {
            listOf(listOf("Error reading file: ${e.message}"))
        }
    }
}