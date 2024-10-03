package com.example.myapplication2

import java.io.Serializable

data class Destinacija(
    val id: Int,
    val naziv: String,
    val slika: String,
    val cena: Double,
    val detalji: String
) : Serializable
