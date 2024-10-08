package com.example.ispit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KnjigaAdapter(private val onItemClick: (Knjiga) -> Unit, private val omiljeneKnjigeIds: MutableSet<Int>, private val saveFavoriteBooks: () -> Unit) :
    RecyclerView.Adapter<KnjigaAdapter.KnjigaViewHolder>() {

    private var knjige: List<Knjiga> = listOf()

    fun updateKnjige(newKnjige: List<Knjiga>) {
        knjige = newKnjige
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnjigaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_knjiga, parent, false)
        return KnjigaViewHolder(view, onItemClick, omiljeneKnjigeIds, saveFavoriteBooks)
    }

    override fun onBindViewHolder(holder: KnjigaViewHolder, position: Int) {
        holder.bind(knjige[position])
    }

    override fun getItemCount() = knjige.size

    class KnjigaViewHolder(itemView: View, private val onItemClick: (Knjiga) -> Unit, private val omiljeneKnjigeIds: MutableSet<Int>, private val saveFavoriteBooks: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val naslov: TextView = itemView.findViewById(R.id.naslov)
        private val autor: TextView = itemView.findViewById(R.id.autor)
        private val zanr: TextView = itemView.findViewById(R.id.zanr)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)

        fun bind(knjiga: Knjiga) {
            naslov.text = knjiga.naslov
            autor.text = knjiga.autor
            zanr.text = knjiga.zanr

            // Prikazivanje omiljenosti knjige
            if (omiljeneKnjigeIds.contains(knjiga.id)) {
                favoriteIcon.setImageResource(R.drawable.baseline_favorite_24) // Ikona za omiljenu knjigu
            } else {
                favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24) // Ikona za neomiljenu knjigu
            }

            // Klik na knjigu
            itemView.setOnClickListener { onItemClick(knjiga) }

            // Klik na ikonu za omiljene
            favoriteIcon.setOnClickListener {
                if (omiljeneKnjigeIds.contains(knjiga.id)) {
                    omiljeneKnjigeIds.remove(knjiga.id)
                    favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    omiljeneKnjigeIds.add(knjiga.id)
                    favoriteIcon.setImageResource(R.drawable.baseline_favorite_24)
                }
                saveFavoriteBooks() // Sačuvaj promene
            }
        }
    }
}

