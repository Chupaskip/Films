package com.example.films.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.films.data.database.FilmDao
import com.example.films.data.database.FilmDatabase
import com.example.films.data.mappers.FilmMapper
import com.example.films.data.mappers.SearchFilmMapper
import com.example.films.data.network.FilmsApi
import com.example.films.domain.entities.Film
import com.example.films.domain.entities.SearchFilm
import com.example.films.domain.repository.FilmRepository
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val filmDao: FilmDao,
    private val filmsApi: FilmsApi
) : FilmRepository {

    private val searchFilmMapper = SearchFilmMapper()
    private val filmMapper = FilmMapper()

    override fun getFavoriteFilms(): LiveData<List<Film>> {
        val filmsFromDb = filmDao.getSavedFilms()
        return filmsFromDb.map { filmDbModels -> filmDbModels.map { filmMapper.mapDbModelToEntity(it) } }
    }

    override suspend fun addFilmToFavorites(film: Film) {
        filmDao.upsertFilm(filmMapper.mapEntityToDbModel(film))
    }

    override suspend fun deleteFilmFromFavorites(film: Film) {
        filmDao.deleteFilm(filmMapper.mapEntityToDbModel(film))
    }

    override suspend fun getSearchFilms(filmName: String): List<SearchFilm> {
        val searchFilmsDto = filmsApi.getSearchFilms(filmName).body()?.films
        return searchFilmsDto?.map { searchFilmMapper.mapDtoToEntity(it) } ?: listOf()
    }

    //TODO("Remove !! symbol")
    override suspend fun getFilm(id: String): Film {
        val filmDto = filmsApi.getFilm(id).body()!!
        return filmMapper.mapDtoToEntity(filmDto)
    }
}