package com.example.films.domain.usecases

import com.example.films.domain.entities.Film
import com.example.films.domain.repository.FilmRepository

class DeleteFilmFromFavoritesUseCase(private val repository: FilmRepository) {

    suspend operator fun invoke(film: Film) {
        repository.deleteFilmFromFavorites(film)
    }
}