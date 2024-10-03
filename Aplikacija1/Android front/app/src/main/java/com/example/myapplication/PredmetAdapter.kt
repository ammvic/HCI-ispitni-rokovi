package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PredmetAdapter(private val predmeti: List<Predmet>) : RecyclerView.Adapter<PredmetAdapter.PredmetViewHolder>() {

    class PredmetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.textViewNaziv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredmetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_predmet, parent, false)
        return PredmetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredmetViewHolder, position: Int) {
        val predmet = predmeti[position]
        holder.naziv.text = predmet.naziv
    }

    override fun getItemCount() = predmeti.size
}
