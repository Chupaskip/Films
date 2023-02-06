package com.example.films.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.films.R
import com.example.films.databinding.FragmentFilmBinding
import com.example.films.databinding.FragmentSearchFilmsBinding
import com.example.films.models.film.Film
import com.example.films.ui.FilmViewModel
import com.example.films.ui.MainActivity
import com.example.films.util.Resource
import com.google.android.material.snackbar.Snackbar


class FilmFragment : Fragment() {

    private lateinit var binding: FragmentFilmBinding

    private lateinit var viewModel: FilmViewModel
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilmBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.film.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    response?.message.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    film = response.data!!
                    bindFilm()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.disableSaveBtn.observe(viewLifecycleOwner) {
            if (it){
                binding.ivSaveFilm.visibility = View.GONE
            }
        }

        binding.ivSaveFilm.setOnClickListener {

            viewModel.upsertFilm(film)
            Snackbar.make(view, "Film is saved", Snackbar.LENGTH_SHORT).show()
        }

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun bindFilm() {
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        binding.apply {
            imageOfFilm.let {
                Glide.with(it)
                    .load(film.poster)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .into(it)
            }
            toolbar.apply {
                title = film.title
                title
            }
            tvFilmPlot.text = film.plot
            dataOfRelease.text = film.released
            tvRuntime.text = film.runtime
            tvAwards.text = film.awards
            tvActors.text = film.actors
            tvWriter.text = film.writer
            tvCountry.text = film.country
            tvImdbRating.text = film.imdbRating

        }
    }


}