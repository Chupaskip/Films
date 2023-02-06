package com.example.films.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.films.repository.FilmRepository

class FilmViewModelProviderFactory(
    val app: Application,
    val repository: FilmRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilmViewModel(repository, app) as T
    }
}