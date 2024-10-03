package com.example.myapplication2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DestinacijaDetailActivity : AppCompatActivity() {

    private lateinit var imgDestinacija: ImageView
    private lateinit var txtNaziv: TextView
    private lateinit var txtCena: TextView
    private lateinit var txtDetalji: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinacija_detail)

        imgDestinacija = findViewById(R.id.imgDestinacija)
        txtNaziv = findViewById(R.id.txtNaziv)
        txtCena = findViewById(R.id.txtCena)
        txtDetalji = findViewById(R.id.txtDetalji)

        val destinacija = intent.getSerializableExtra("destinacija") as Destinacija

        txtNaziv.text = destinacija.naziv
        txtCena.text = destinacija.cena.toString()
        txtDetalji.text = destinacija.detalji
        Glide.with(this).load(destinacija.slika).into(imgDestinacija)
    }
}