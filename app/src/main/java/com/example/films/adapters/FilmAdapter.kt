package com.example.films.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.R
import com.example.films.databinding.ItemFilmBinding
import com.example.films.models.search.Search

class FilmAdapter(
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(val viewBinding: ItemFilmBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
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
            Glide.with(root).load(film.image)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(image)
            root.setOnClickListener {
                onItemClickListener?.let { it(film) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Search) -> Unit)? = null

    fun setOnItemClickListener(listener: (Search) -> Unit) {
        onItemClickListener = listener
    }

}