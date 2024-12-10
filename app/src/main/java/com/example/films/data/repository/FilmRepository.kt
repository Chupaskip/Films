package com.example.films.data.repository

import com.example.films.data.database.FilmDatabase
import com.example.films.data.models.film.Film
import com.example.films.data.network.FilmsApi
import com.example.films.data.network.RetrofitInstance
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val filmDatabase: FilmDatabase,
    private val filmsApi: FilmsApi
) {

    suspend fun getSearchFilms(filmName: String) = filmsApi.getSearchFilms(filmName)
    suspend fun getFilm(id: String) = filmsApi.getFilm(id)

    suspend fun upsertFilm(film: Film) = filmDatabase.getFilmDao().upsertFilm(film)
    suspend fun deleteFilm(film: Film) = filmDatabase.getFilmDao().deleteFilm(film)
    fun getSavedFilms() = filmDatabase.getFilmDao().getSavedFilms()
}