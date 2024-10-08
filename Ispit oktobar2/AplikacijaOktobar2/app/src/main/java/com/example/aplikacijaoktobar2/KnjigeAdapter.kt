package com.example.aplikacijaoktobar2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KnjigeAdapter(
    private var knjige: List<Knjiga>,
    private val onClick: (Knjiga) -> Unit
) : RecyclerView.Adapter<KnjigeAdapter.KnjigaViewHolder>() {

    inner class KnjigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val naslov: TextView = itemView.findViewById(R.id.naslov)

        fun bind(knjiga: Knjiga) {
            naslov.text = knjiga.naslov
            itemView.setOnClickListener {
                onClick(knjiga) // Pozivamo onClick lambda kada se stavka pritisne
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnjigaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_knjiga, parent, false)
        return KnjigaViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnjigaViewHolder, position: Int) {
        holder.bind(knjige[position])
    }

    override fun getItemCount(): Int = knjige.size

    fun updateKnjige(newKnjige: List<Knjiga>) {
        knjige = newKnjige
        notifyDataSetChanged()
    }

    fun sortByAuthor() {
        knjige = knjige.sortedBy { it.autor } // Pretpostavljam da je `autor` svojstvo
        notifyDataSetChanged()
    }

    fun sortByDate() {
        knjige = knjige.sortedBy { it.datum_izdavanja } // Pretpostavljam da je `datumObjavljivanja` svojstvo
        notifyDataSetChanged()
    }
}

