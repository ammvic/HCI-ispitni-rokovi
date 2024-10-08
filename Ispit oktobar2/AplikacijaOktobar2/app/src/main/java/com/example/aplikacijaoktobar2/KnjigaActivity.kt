package com.example.aplikacijaoktobar2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KnjigaActivity : AppCompatActivity() {

    private lateinit var knjigaApi: KnjigaApi
    private lateinit var knjigeAdapter: KnjigeAdapter
    private val omiljeneKnjige = mutableListOf<Knjiga>() // Lista omiljenih knjiga

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knjige)

        val inputNaslov: EditText = findViewById(R.id.inputNaslov)
        val inputAutor: EditText = findViewById(R.id.inputAutor)
        val inputZanr: EditText = findViewById(R.id.inputZanr)
        val btnPretraga: Button = findViewById(R.id.btnPretraga)

        // Dugme za pretragu
        btnPretraga.setOnClickListener {
            val naslov = inputNaslov.text.toString().trim()
            val autor = inputAutor.text.toString().trim()
            val zanr = inputZanr.text.toString().trim()
            if (naslov.isEmpty() && autor.isEmpty() && zanr.isEmpty()) {
                fetchAllKnjige()
            } else {
                searchKnjige(naslov, autor, zanr)
            }
        }

        // Postavljanje RecyclerView-a i adaptera
        knjigeAdapter = KnjigeAdapter(listOf()) { knjiga ->
            val intent = Intent(this, DetaljiKnjigeActivity::class.java)
            intent.putExtra("knjigaId", knjiga.id)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewKnjige)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = knjigeAdapter

        knjigaApi = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5076/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KnjigaApi::class.java)

        fetchAllKnjige()  // Prikaz svih knjiga pri učitavanju

        // Povezivanje sa NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            true
        }
    }

    private fun fetchAllKnjige() {
        lifecycleScope.launch {
            try {
                val response = knjigaApi.getKnjige()
                knjigeAdapter.updateKnjige(response)
            } catch (e: Exception) {
                Log.e("KnjigeActivity", "Error fetching knjige: ${e.message}")
                Toast.makeText(this@KnjigaActivity, "Greška pri dohvatanju podataka", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun searchKnjige(naslov: String, autor: String, zanr: String) {
        lifecycleScope.launch {
            try {
                val response = knjigaApi.searchKnjige(naslov, autor, zanr)
                knjigeAdapter.updateKnjige(response)
            } catch (e: Exception) {
                Log.e("KnjigeActivity", "Error searching knjige: ${e.message}")
                Toast.makeText(this@KnjigaActivity, "Greška pri pretrazi", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_all_books -> fetchAllKnjige() // Ponovo učitaj sve knjige
            R.id.nav_sort_by_author -> sortKnjigeByAuthor()
            R.id.nav_sort_by_date -> sortKnjigeByDate()
            R.id.nav_favorite_books -> showFavoriteKnjige()
        }
    }

    private fun sortKnjigeByAuthor() {
        knjigeAdapter.sortByAuthor()
    }

    private fun sortKnjigeByDate() {
        knjigeAdapter.sortByDate()
    }

    private fun showFavoriteKnjige() {
        // Ažuriraj adapter sa omiljenim knjigama
        knjigeAdapter.updateKnjige(omiljeneKnjige)
    }

    // Metoda za dodavanje ili uklanjanje knjige iz omiljenih
    fun toggleFavorite(knjiga: Knjiga) {
        if (omiljeneKnjige.contains(knjiga)) {
            omiljeneKnjige.remove(knjiga)
            Toast.makeText(this, "${knjiga.naslov} je uklonjena iz omiljenih.", Toast.LENGTH_SHORT).show()
        } else {
            omiljeneKnjige.add(knjiga)
            Toast.makeText(this, "${knjiga.naslov} je dodata u omiljene.", Toast.LENGTH_SHORT).show()
        }
    }
}


