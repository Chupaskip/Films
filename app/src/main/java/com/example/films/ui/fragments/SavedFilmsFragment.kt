package com.example.films.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.films.R
import com.example.films.adapters.FilmAdapter
import com.example.films.databinding.FragmentSavedFilmsBinding
import com.example.films.models.film.Film
import com.example.films.models.film.toSearch
import com.example.films.ui.FilmViewModel
import com.example.films.ui.MainActivity
import com.example.films.util.Resource
import com.google.android.material.snackbar.Snackbar


class SavedFilmsFragment : Fragment() {

    private lateinit var binding: FragmentSavedFilmsBinding
    private lateinit var viewModel: FilmViewModel
    private lateinit var filmAdapter: FilmAdapter
    private lateinit var films: List<Film>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedFilmsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
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
        filmAdapter = FilmAdapter()
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