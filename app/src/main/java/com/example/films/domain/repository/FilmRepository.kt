package com.example.films.domain.repository

import androidx.lifecycle.LiveData
import com.example.films.domain.entities.Film
import com.example.films.domain.entities.SearchFilm
import com.example.films.util.State

interface FilmRepository {

    suspend fun getSearchFilms(filmName: String): State<List<SearchFilm>>

    suspend fun getFilm(id: String): State<Film>

    fun getFavoriteFilms(): LiveData<List<Film>>

    suspend fun addFilmToFavorites(film: Film)

    suspend fun deleteFilmFromFavorites(film: Film)
}