package com.example.films.repository

import com.example.films.database.FilmDatabase
import com.example.films.models.film.Film
import com.example.films.network.RetrofitInstance

class FilmRepository(
    private val filmDatabase: FilmDatabase
) {

    suspend fun getSearchFilms(filmName: String) = RetrofitInstance.api.getSearchFilms(filmName)
    suspend fun getFilm(id: String) = RetrofitInstance.api.getFilm(id)

    suspend fun upsertFilm(film: Film) = filmDatabase.getFilmDao().upsertFilm(film)
    suspend fun deleteFilm(film: Film) = filmDatabase.getFilmDao().deleteFilm(film)
    fun getSavedFilms() = filmDatabase.getFilmDao().getSavedFilms()
}