package com.example.films.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.films.R
import com.example.films.presentation.adapters.FilmAdapter
import com.example.films.databinding.FragmentSearchFilmsBinding
import com.example.films.util.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFilmsFragment : BaseFragment<FragmentSearchFilmsBinding>() {

    @Inject
    lateinit var filmAdapter: FilmAdapter

    override fun viewBinding() = FragmentSearchFilmsBinding.inflate(layoutInflater)

    private val sharedPref by lazy {
        activity?.getPreferences(Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        filmAdapter.setOnItemClickListener {
            viewModel.getFilm(it.id)
            findNavController().navigate(
                R.id.action_searchFilmsFragment_to_filmFragment
            )
        }
        viewModel.searchFilmsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Error -> {
                    state.message.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.filmsRefresher.isRefreshing = false
                }

                is State.Loading -> {
                    if (!binding.filmsRefresher.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }

                is State.Success -> {
                    filmAdapter.submitList(state.data)
                    binding.filmsRefresher.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                }
            }
        }



        binding.filmSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {
                    viewModel.searchText.postValue(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.searchText.observe(viewLifecycleOwner) {
            with(sharedPref?.edit()) {
                this?.putString(getString(R.string.lastSearch), viewModel.searchText.value)
                this?.apply()
            }
            viewModel.getFilms(it)
        }
        binding.filmsRefresher.setOnRefreshListener {
            viewModel.getFilms(viewModel.searchText.value ?: "")
        }

    }

    private fun setupRecyclerView() {
        binding.rvFilms.apply {
            adapter = filmAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }


}