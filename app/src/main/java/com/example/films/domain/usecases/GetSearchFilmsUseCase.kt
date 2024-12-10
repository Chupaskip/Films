package com.example.films.domain.usecases

import com.example.films.domain.repository.FilmRepository

class GetSearchFilmsUseCase(private val repository: FilmRepository) {

    suspend fun getSearchFilms(filmName: String) = repository.getSearchFilms(filmName)
}