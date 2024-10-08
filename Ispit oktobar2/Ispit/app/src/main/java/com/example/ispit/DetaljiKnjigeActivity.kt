package com.example.ispit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

class DetaljiKnjigeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalji_knjige)

        val knjigaId = intent.getIntExtra("knjigaId", -1)

        if (knjigaId != -1) {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)  // Postavlja timeout za povezivanje
                .writeTimeout(120, TimeUnit.SECONDS)    // Postavlja timeout za pisanje podataka
                .readTimeout(120, TimeUnit.SECONDS)     // Postavlja timeout za čitanje podataka
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5076/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(KnjigaApi::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                val knjiga = api.getKnjiga(knjigaId)
                runOnUiThread {
                    // Popunjavanje UI sa podacima knjige
                    findViewById<TextView>(R.id.naslov).text = knjiga.naslov
                    findViewById<TextView>(R.id.autor).text = knjiga.autor
                    findViewById<TextView>(R.id.zanr).text = knjiga.zanr
                    findViewById<TextView>(R.id.opis).text = knjiga.opis
                    findViewById<TextView>(R.id.datumIzdavanja).text = knjiga.datum_Izdavanja
                }
            }
        }
    }
}
