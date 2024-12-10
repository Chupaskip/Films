package com.example.films.domain.usecases

import com.example.films.domain.repository.FilmRepository

class GetFavoritesFilmsUseCase(private val repository: FilmRepository) {

    operator fun invoke() = repository.getFavoriteFilms()
}