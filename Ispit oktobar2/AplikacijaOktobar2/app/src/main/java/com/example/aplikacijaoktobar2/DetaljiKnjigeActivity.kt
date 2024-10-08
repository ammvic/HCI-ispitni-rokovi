package com.example.aplikacijaoktobar2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetaljiKnjigeActivity : AppCompatActivity() {

    private lateinit var knjigaApi: KnjigaApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalji_knjige)

        val knjigaId = intent.getIntExtra("knjigaId", -1)
        knjigaApi = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5076/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KnjigaApi::class.java)

        if (knjigaId != -1) {
            fetchKnjigaDetalji(knjigaId)
        }
    }

    private fun fetchKnjigaDetalji(id: Int) {
        lifecycleScope.launch {
            try {
                val knjiga = knjigaApi.getKnjiga(id)
                findViewById<TextView>(R.id.naslov).text = knjiga.naslov
                findViewById<TextView>(R.id.autor).text = knjiga.autor
                findViewById<TextView>(R.id.zanr).text = knjiga.zanr
                findViewById<TextView>(R.id.opis).text = knjiga.opis
                findViewById<TextView>(R.id.datum_izdavanja).text = knjiga.datum_izdavanja
            } catch (e: Exception) {
                Log.e("DetaljiKnjigeActivity", "Error fetching knjiga details: ${e.message}")
                Toast.makeText(this@DetaljiKnjigeActivity, "Greška pri dohvatanju podataka", Toast.LENGTH_LONG).show()
            }
        }
    }
}

