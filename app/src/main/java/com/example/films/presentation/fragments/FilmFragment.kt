package com.example.films.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.films.databinding.FragmentFilmBinding
import com.example.films.domain.entities.Film
import com.example.films.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilmFragment : BaseFragment<FragmentFilmBinding>() {

    override fun viewBinding() = FragmentFilmBinding.inflate(layoutInflater)

    @Inject
    lateinit var glide: RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.filmState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Error -> {
                    state.message.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    state.data?.let { bindFilm(film = it) }
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
            viewModel.filmState.value?.data?.also { film ->
                viewModel.addFilmToFavorites(film)
                Snackbar.make(view, "Film is saved", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun bindFilm(film: Film) {
        film.apply {
            binding.apply {
                imageOfFilm.let {
                    glide.load(poster).into(it)
                }
                binding.collapsingToolbar.title = title
                tvFilmPlot.text = plot
                dataOfRelease.text = released
                tvRuntime.text = runtime
                tvAwards.text = awards
                tvActors.text = actors
                tvWriter.text = writer
                tvCountry.text = country
                tvImdbRating.text = imdbRating
            }
        }
    }
}