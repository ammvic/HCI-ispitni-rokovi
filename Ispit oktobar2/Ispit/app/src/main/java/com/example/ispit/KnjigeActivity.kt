package com.example.ispit

import android.content.Intent
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class KnjigeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var knjigaAdapter: KnjigaAdapter
    private lateinit var api: KnjigaApi
    private val omiljeneKnjigeIds = mutableSetOf<Int>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knjige)

        sharedPreferences = getSharedPreferences("KnjigePrefs", MODE_PRIVATE)
        loadFavoriteBooks()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchInput = findViewById<EditText>(R.id.searchInput)

        recyclerView.layoutManager = LinearLayoutManager(this)
        knjigaAdapter = KnjigaAdapter(
            onItemClick = { knjiga ->
                val intent = Intent(this, DetaljiKnjigeActivity::class.java)
                intent.putExtra("knjigaId", knjiga.id)
                startActivity(intent)
            },
            omiljeneKnjigeIds = omiljeneKnjigeIds,
            saveFavoriteBooks = { saveFavoriteBooks() }
        )
        recyclerView.adapter = knjigaAdapter

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(180, TimeUnit.SECONDS)  // Postavlja timeout za povezivanje
            .writeTimeout(180, TimeUnit.SECONDS)    // Postavlja timeout za pisanje podataka
            .readTimeout(180, TimeUnit.SECONDS)     // Postavlja timeout za čitanje podataka
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5076/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(KnjigaApi::class.java)

        // Fetching knjige from the API
        CoroutineScope(Dispatchers.IO).launch {
            val knjige = api.getKnjige()
            runOnUiThread {
                knjigaAdapter.updateKnjige(knjige)
            }
        }

        // Search input listener
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val filteredKnjige = api.searchKnjige(s.toString())
                    runOnUiThread {
                        knjigaAdapter.updateKnjige(filteredKnjige)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


        private fun loadFavoriteBooks() {
        // Učitaj omiljene knjige iz SharedPreferences
        val favoritesSet = sharedPreferences.getStringSet("omiljeneKnjige", emptySet())
        omiljeneKnjigeIds.addAll(favoritesSet?.map { it.toInt() } ?: emptyList())
    }

    private fun saveFavoriteBooks() {
        // Sačuvaj omiljene knjige u SharedPreferences
        sharedPreferences.edit().putStringSet("omiljeneKnjige", omiljeneKnjigeIds.map { it.toString() }.toSet()).apply()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Otvara ili zatvara navigacioni panel kada se klikne na ikonicu hamburgera
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.navigationView))) {
                drawerLayout.closeDrawer(findViewById(R.id.navigationView))
            } else {
                drawerLayout.openDrawer(findViewById(R.id.navigationView))
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("KnjigeActivity", "Selected item: ${item.title}")
        when (item.itemId) {
            R.id.nav_all_books -> {
                // Prikaži sve knjige (pozovi API za sve knjige)
                CoroutineScope(Dispatchers.IO).launch {
                    val knjige = api.getKnjige()
                    runOnUiThread {
                        knjigaAdapter.updateKnjige(knjige)
                    }
                }
            }

            R.id.nav_favorite_books -> {
                // Prikazivanje omiljenih knjiga
                CoroutineScope(Dispatchers.IO).launch {
                    // Dobavi sve knjige ponovo
                    val sveKnjige = api.getKnjige() // Pozovi API da dobiješ sve knjige
                    val omiljeneKnjige = sveKnjige.filter { omiljeneKnjigeIds.contains(it.id) }
                    runOnUiThread {
                        knjigaAdapter.updateKnjige(omiljeneKnjige) // Ažuriraj adapter sa omiljenim knjigama
                    }
                }
            }

            R.id.nav_sort -> {
                // Prikazivanje dijaloga za izbor načina sortiranja
                showSortDialog()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun showSortDialog() {
        val options = arrayOf("Sortiraj po datumu", "Sortiraj po autoru")
        AlertDialog.Builder(this)
            .setTitle("Izaberi način sortiranja")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> sortBooksByDate() // Sortiraj po datumu
                    1 -> sortBooksByAuthor() // Sortiraj po autoru
                }
            }
            .show()
    }

    private fun sortBooksByDate() {
        CoroutineScope(Dispatchers.IO).launch {
            val knjige = api.getKnjige() // Ponovo pozovi API da dobiješ sve knjige
            val sortedByDate = knjige.sortedBy { it.datum_Izdavanja } // Sortiraj po datumu
            runOnUiThread {
                knjigaAdapter.updateKnjige(sortedByDate) // Prikazujemo knjige sortirane po datumu
            }
        }
    }

    private fun sortBooksByAuthor() {
        CoroutineScope(Dispatchers.IO).launch {
            val knjige = api.getKnjige() // Ponovo pozovi API da dobiješ sve knjige
            val sortedByAuthor = knjige.sortedBy { it.autor } // Sortiraj po autoru
            runOnUiThread {
                knjigaAdapter.updateKnjige(sortedByAuthor) // Prikazujemo knjige sortirane po autoru
            }
        }
    }
}
