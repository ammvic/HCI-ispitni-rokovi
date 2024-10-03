package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PredmetiActivity : AppCompatActivity() {

    private lateinit var fakultetApi: FakultetApi
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PredmetAdapter
    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predmeti)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PredmetAdapter(emptyList())
        recyclerView.adapter = adapter

        emptyMessage = findViewById(R.id.empty_message) // Inicijalizacija TextView

        // Retrofit instanca
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5134/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fakultetApi = retrofit.create(FakultetApi::class.java)

        val profesorId = intent.getIntExtra("profesorId", -1)
        if (profesorId != -1) {
            loadPredmeti(profesorId)
        }
    }

    private fun loadPredmeti(profesorId: Int) {
        fakultetApi.getPredmeti(profesorId).enqueue(object : Callback<List<Predmet>> {
            override fun onResponse(call: Call<List<Predmet>>, response: Response<List<Predmet>>) {
                if (response.isSuccessful) {
                    val predmeti = response.body() ?: emptyList()

                    // Ažuriraj adapter sa novim podacima
                    adapter = PredmetAdapter(predmeti)
                    recyclerView.adapter = adapter

                    // Proveri da li je lista prazna
                    if (predmeti.isEmpty()) {
                        emptyMessage.visibility = View.VISIBLE // Prikaži poruku
                        recyclerView.visibility = View.GONE // Sakrij RecyclerView
                    } else {
                        emptyMessage.visibility = View.GONE // Sakrij poruku
                        recyclerView.visibility = View.VISIBLE // Prikaži RecyclerView
                    }
                }
            }

            override fun onFailure(call: Call<List<Predmet>>, t: Throwable) {
                emptyMessage.text = "Error: ${t.message}"
                emptyMessage.visibility = View.VISIBLE // Prikaži grešku
                recyclerView.visibility = View.GONE // Sakrij RecyclerView
            }
        })
    }
}


