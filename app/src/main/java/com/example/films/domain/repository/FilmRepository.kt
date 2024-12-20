package com.example.films.domain.repository

import androidx.lifecycle.LiveData
import com.example.films.domain.entities.Film
import com.example.films.util.Resource

interface FilmRepository {

    suspend fun getSearchFilms(filmName: String): Resource<List<Film>>

    suspend fun getFilm(id: String): Resource<Film>

    fun getFavoriteFilms(): LiveData<List<Film>>

    suspend fun addFilmToFavorites(film: Film)

    suspend fun deleteFilmFromFavorites(film: Film)
}