package com.example.films.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.films.R
import com.example.films.databinding.FragmentSavedFilmsBinding
import com.example.films.domain.entities.Film
import com.example.films.domain.entities.SearchFilm
import com.example.films.presentation.adapters.FilmAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedFilmsFragment : BaseFragment<FragmentSavedFilmsBinding>() {

    @Inject
    lateinit var filmAdapter: FilmAdapter
    private lateinit var films: List<Film>

    override fun viewBinding() = FragmentSavedFilmsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.disableSaveBtn.postValue(true)
        filmAdapter.setOnItemClickListener {
            val chosenFilm =
                films.find { film -> film.id == it.id }
            viewModel.film.postValue(chosenFilm)
            findNavController().navigate(
                R.id.action_savedFilmsFragment_to_filmFragment
            )
        }

        viewModel.favoriteFilms.observe(viewLifecycleOwner) {
            films = it
            filmAdapter.submitList(films.map { film ->
                SearchFilm(
                    film.id,
                    film.poster,
                    film.title
                )
            })
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val search = filmAdapter.currentList[position]
                val film = films.find { film -> film.id == search.id }
                film?.also {
                    viewModel.deleteFilmFromFavorites(film)
                    Snackbar.make(
                        view,
                        "Film was deleted from \nSaved successfully",
                        Snackbar.LENGTH_LONG
                    ).apply {
                        setAction("Undo") {
                            viewModel.addFilmToFavorites(film)
                        }
                        show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedFilms)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSavedFilms.apply {
            adapter = filmAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.disableSaveBtn.postValue(false)
    }
}