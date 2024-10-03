package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.Destinacija
import com.example.myapplication2.DestinacijaAdapter
import com.example.myapplication2.DestinacijaApi
import com.example.myapplication2.DestinacijaDetailActivity
import com.example.myapplication2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var destinacijaApi: DestinacijaApi
    private lateinit var destinacijaListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var destinacijaAdapter: DestinacijaAdapter
    private var destinacijaList: List<Destinacija> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        destinacijaListView = findViewById(R.id.destinacijaListView)
        searchEditText = findViewById(R.id.searchEditText)

        // Retrofit instanca
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5106/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        destinacijaApi = retrofit.create(DestinacijaApi::class.java)

        // Load data
        loadDestinacije()

        // Set up search filter
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDestinacije(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadDestinacije() {
        // Pokretanje API poziva u korutini
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = destinacijaApi.getDestinacije()
                destinacijaList = response

                // Ažuriranje UI-ja mora biti u glavnoj niti
                runOnUiThread {
                    destinacijaAdapter = DestinacijaAdapter(this@MainActivity, destinacijaList)
                    destinacijaListView.adapter = destinacijaAdapter

                    destinacijaListView.setOnItemClickListener { _, _, position, _ ->
                        val selectedDestinacija = destinacijaList[position]
                        val intent = Intent(this@MainActivity, DestinacijaDetailActivity::class.java)
                        intent.putExtra("destinacija", selectedDestinacija)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun filterDestinacije(query: String) {
        val filteredList = destinacijaList.filter {
            it.naziv.contains(query, ignoreCase = true)
        }
        destinacijaAdapter.updateList(filteredList)
    }
}
