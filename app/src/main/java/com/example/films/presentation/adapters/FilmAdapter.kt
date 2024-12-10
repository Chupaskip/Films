package com.example.films.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.films.databinding.ItemFilmBinding
import com.example.films.data.network.models.search.SearchFilmDto
import javax.inject.Inject

class FilmAdapter @Inject constructor(
    private val glide: RequestManager,
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(val viewBinding: ItemFilmBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<SearchFilmDto>() {
        override fun areItemsTheSame(oldItem: SearchFilmDto, newItem: SearchFilmDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchFilmDto, newItem: SearchFilmDto): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding =
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = differ.currentList[position]

        holder.viewBinding.apply {
            tvFilmName.text = film.title
            glide.load(film.poster).into(image)
            root.setOnClickListener {
                onItemClickListener?.let { it(film) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((SearchFilmDto) -> Unit)? = null

    fun setOnItemClickListener(listener: (SearchFilmDto) -> Unit) {
        onItemClickListener = listener
    }

}