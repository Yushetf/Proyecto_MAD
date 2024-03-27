package es.proyectoMad

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.upm.btb.helloworldkt.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import es.proyectoMad.persistence.room.AppDatabase

class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"

    private lateinit var listView: ListView
    private lateinit var adapter: BarAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d(TAG, "onCreate: The activity is being created.")

        val deleteAllBarsButton: Button = findViewById(R.id.deleteAllBarsButton)
        deleteAllBarsButton.setOnClickListener {
            deleteAllBarsFromDatabase()
        }

        // ButtomNavigationMenu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            val currentActivity = this::class.java.simpleName
            when (item.itemId) {
                R.id.navigation_home -> if (currentActivity != MainActivity::class.java.simpleName) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.navigation_map -> if (currentActivity != OpenStreetMapActivity::class.java.simpleName) {
                    startActivity(Intent(this, OpenStreetMapActivity::class.java))
                }
                R.id.navigation_list -> if (currentActivity != SecondActivity::class.java.simpleName) {
                    startActivity(Intent(this, SecondActivity::class.java))
                }
            }
            true
        }

        // Inflate heading and add to ListView
        listView = findViewById(R.id.listViewBars)

        // Init adapter
        adapter = BarAdapter(this, mutableListOf())
        listView.adapter = adapter

        // Init database
        database = AppDatabase.getInstance(this)

        // Listener para la selección de un bar en la lista
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedBarName = adapter.getItem(position)
            lifecycleScope.launch(Dispatchers.IO) {
                val selectedBarEntity = selectedBarName?.let { database.barDao().getBarByName(it) }
                withContext(Dispatchers.Main) {
                    if (selectedBarEntity != null) {
                        Log.d(TAG, "Bar no es null en secondactivity")
                        val intent = Intent(this@SecondActivity, BarDetailsActivity::class.java)
                        intent.putExtra("barEntity", selectedBarEntity)
                        startActivity(intent)
                    } else {
                        Log.d(TAG, "Bar es null en secondactivity")
                    }
                }
            }
        }

        // Configurar TextWatcher para la búsqueda
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                filterBars(searchText)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No es necesario implementar este método
            }
        })
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            val barNamesList = loadBarsFromDatabase()
            withContext(Dispatchers.Main) {
                adapter.updateData(barNamesList)
            }
        }
    }

    private suspend fun loadBarsFromDatabase(): List<String> {
        val barNamesSet = HashSet<String>()
        val barsList = database.barDao().getAllBars()
        barsList.forEach { barEntity ->
            barNamesSet.add(barEntity.name)
        }
        return barNamesSet.toList().sorted()
    }

    private fun filterBars(searchText: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val allBars = loadBarsFromDatabase()
            val filteredBars = allBars.filter { barName ->
                barName.contains(searchText, ignoreCase = true)
            }
            withContext(Dispatchers.Main) {
                adapter.updateData(filteredBars)
            }
        }
    }

    private fun deleteAllBarsFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            database.barDao().deleteAllBars()
            withContext(Dispatchers.Main) {
                // Actualizar la lista después de eliminar los bares
                adapter.updateData(emptyList())
                Toast.makeText(this@SecondActivity, "All bars deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private class BarAdapter(context: Context, private val barList: MutableList<String>) :
        ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, barList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = barList[position]
            return view
        }

        fun updateData(newData: List<String>) {
            barList.clear()
            barList.addAll(newData)
            notifyDataSetChanged()
        }
    }
}
