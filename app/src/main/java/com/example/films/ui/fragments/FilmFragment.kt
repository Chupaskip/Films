package com.example.films.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.films.R
import com.example.films.databinding.FragmentFilmBinding
import com.example.films.models.film.Film
import com.example.films.ui.MainActivity
import com.example.films.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilmFragment : BaseFragment<FragmentFilmBinding>() {

    override fun viewBinding() = FragmentFilmBinding.inflate(layoutInflater)

    private lateinit var film: Film

    @Inject
    lateinit var glide: RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.film.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    response.message.let {
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
            if (it) {
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
                glide.load(film.poster).into(it)
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