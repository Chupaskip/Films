package com.example.films.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.example.films.R
import com.example.films.databinding.FragmentFilmBinding
import com.example.films.domain.entities.Film
import com.example.films.presentation.MainActivity
import com.example.films.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilmFragment : BaseFragment<FragmentFilmBinding>() {

    override fun viewBinding() = FragmentFilmBinding.inflate(layoutInflater)

    private var film: Film? = null

    @Inject
    lateinit var glide: RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.film.observe(viewLifecycleOwner) { film ->
//            when (response) {
//                is Resource.Error -> {
//                    response.message.let {
//                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    binding.progressBar.visibility = View.GONE
//                }
//                is Resource.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is Resource.Success -> {
//                    film = response.data!!
            this.film = film
            bindFilm()
            binding.progressBar.visibility = View.GONE
//                }
//            }
        }

        viewModel.disableSaveBtn.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivSaveFilm.visibility = View.GONE
            }
        }

        binding.ivSaveFilm.setOnClickListener {
            film?.also { film ->
                viewModel.addFilmToFavorites(film)
                Snackbar.make(view, "Film is saved", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun bindFilm() {
        film?.also { film ->
            binding.apply {
                imageOfFilm.let {
                    glide.load(film.poster).into(it)
                }
                binding.collapsingToolbar.title = film.title
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
}