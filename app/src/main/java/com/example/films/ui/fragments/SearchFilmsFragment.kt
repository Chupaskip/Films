package com.example.films.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.films.R
import com.example.films.adapters.FilmAdapter
import com.example.films.databinding.FragmentSearchFilmsBinding
import com.example.films.util.Resource
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
            if (viewModel.hasInternet()) {
                viewModel.getFilm(it.id)
                findNavController().navigate(
                    R.id.action_searchFilmsFragment_to_filmFragment
                )
            } else {
                Toast.makeText(
                    activity,
                    "An error occurred: no internet connection",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
        viewModel.films.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    response.message.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.filmsRefresher.isRefreshing = false
                }
                is Resource.Loading -> {
                    if (!binding.filmsRefresher.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    filmAdapter.differ.submitList(response.data?.films)
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

        val lastSearch =
            sharedPref?.getString(getString(R.string.lastSearch), viewModel.getRandomWord())
        viewModel.searchText.postValue(lastSearch)

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