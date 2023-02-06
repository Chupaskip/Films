package com.example.films.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.films.R
import com.example.films.database.FilmDatabase
import com.example.films.databinding.ActivityMainBinding
import com.example.films.repository.FilmRepository

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val repository = FilmRepository(FilmDatabase(this))
        val viewModelProviderFactory = FilmViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[FilmViewModel::class.java]

        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}