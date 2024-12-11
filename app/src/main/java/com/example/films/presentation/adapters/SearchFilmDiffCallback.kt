package com.example.films.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.films.domain.entities.SearchFilm

class SearchFilmDiffCallback:DiffUtil.ItemCallback<SearchFilm>() {
    override fun areItemsTheSame(oldItem: SearchFilm, newItem: SearchFilm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchFilm, newItem: SearchFilm): Boolean {
        return oldItem == newItem
    }
}