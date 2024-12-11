package com.example.films.domain.usecases

import com.example.films.domain.repository.FilmRepository

class GetSearchFilmsUseCase(private val repository: FilmRepository) {

    suspend operator fun invoke(filmName: String) = repository.getSearchFilms(filmName)
}