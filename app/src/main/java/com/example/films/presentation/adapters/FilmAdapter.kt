package com.example.films.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.example.films.databinding.ItemFilmBinding
import com.example.films.domain.entities.Film
import javax.inject.Inject

class FilmAdapter @Inject constructor(
    private val glide: RequestManager,
) : ListAdapter<Film, FilmViewHolder>(SearchFilmDiffCallback()) {

    private var onItemClickListener: ((Film) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding =
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = currentList[position]

        holder.viewBinding.apply {
            tvFilmName.text = film.title
            glide.load(film.poster).into(image)
            root.setOnClickListener {
                onItemClickListener?.let { it(film) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Film) -> Unit) {
        onItemClickListener = listener
    }
}