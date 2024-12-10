package com.example.films.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.films.R
import com.example.films.presentation.adapters.FilmAdapter
import com.example.films.databinding.FragmentSavedFilmsBinding
import com.example.films.data.models.film.Film
import com.example.films.data.models.film.toSearch
import com.example.films.util.Resource
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
                films.find { film -> film.id == it.id } ?: Film()
            viewModel.film.postValue(Resource.Success(chosenFilm))
            findNavController().navigate(
                R.id.action_savedFilmsFragment_to_filmFragment
            )
        }

        viewModel.getSavedFilms().observe(viewLifecycleOwner) {
            films = it
            filmAdapter.differ.submitList(films.map { film -> film.toSearch() })
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
                val search = filmAdapter.differ.currentList[position]
                val film = films.find { film -> film.id == search.id } ?: Film()
                viewModel.deleteFilm(film)
                Snackbar.make(
                    view,
                    "Film was deleted from \nSaved successfully",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction("Undo") {
                        viewModel.upsertFilm(film)
                    }
                    show()
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