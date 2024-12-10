package com.example.films.domain.usecases

import com.example.films.domain.repository.FilmRepository

class GetFilmUseCase(private val repository: FilmRepository) {

    suspend operator fun invoke(id:String) = repository.getFilm(id)
}