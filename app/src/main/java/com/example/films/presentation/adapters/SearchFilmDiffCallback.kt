package com.example.films.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.films.domain.entities.Film

class SearchFilmDiffCallback:DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem == newItem
    }
}