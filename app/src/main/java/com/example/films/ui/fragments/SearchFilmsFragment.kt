package com.example.films.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.films.R
import com.example.films.adapters.FilmAdapter
import com.example.films.databinding.FragmentSearchFilmsBinding
import com.example.films.ui.FilmViewModel
import com.example.films.ui.MainActivity
import com.example.films.util.Resource


class SearchFilmsFragment : Fragment() {

    private lateinit var binding: FragmentSearchFilmsBinding
    private lateinit var viewModel: FilmViewModel
    private lateinit var filmAdapter: FilmAdapter

    private val sharedPref by lazy {
        activity?.getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchFilmsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
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
                    response?.message.let {
                        Toast.makeText(activity, "An error occurred: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.filmsRefresher.isRefreshing = false
                }
                is Resource.Loading -> {
                    if(!binding.filmsRefresher.isRefreshing){
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

        val lastSearch = sharedPref?.getString(getString(R.string.lastSearch), viewModel.getRandomWord())
        viewModel.searchText.postValue(lastSearch)

        viewModel.searchText.observe(viewLifecycleOwner) {
            with (sharedPref?.edit()) {
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
        filmAdapter = FilmAdapter()
        binding.rvFilms.apply {
            adapter = filmAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }


}