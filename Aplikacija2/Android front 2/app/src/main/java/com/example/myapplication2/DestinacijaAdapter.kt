package com.example.myapplication2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DestinacijaAdapter(private val context: Context, private var destinacije: List<Destinacija>) : BaseAdapter() {

    override fun getCount(): Int = destinacije.size

    override fun getItem(position: Int): Any = destinacije[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        val txtNaziv = view.findViewById<TextView>(android.R.id.text1)
        val txtCena = view.findViewById<TextView>(android.R.id.text2)

        val destinacija = destinacije[position]
        txtNaziv.text = destinacija.naziv
        txtCena.text = destinacija.cena.toString()

        return view
    }

    fun updateList(newDestinacije: List<Destinacija>) {
        destinacije = newDestinacije
        notifyDataSetChanged()
    }
}