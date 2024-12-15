package com.example.films.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.films.data.database.FilmDao
import com.example.films.data.mappers.FilmMapper
import com.example.films.data.mappers.SearchFilmMapper
import com.example.films.data.network.FilmsApi
import com.example.films.domain.entities.Film
import com.example.films.domain.entities.SearchFilm
import com.example.films.domain.repository.FilmRepository
import com.example.films.util.State
import retrofit2.HttpException
import java.io.IOException
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

    override suspend fun getSearchFilms(filmName: String): State<List<SearchFilm>> {
        return try {
            val response = filmsApi.getSearchFilms(filmName)

            if (!response.isSuccessful) {
                return State.Error("Request failed with status code: ${response.code()}")
            }

            val searchFilmsDto = filmsApi.getSearchFilms(filmName).body()?.films
                ?: return State.Error("Response body is null")

            State.Success(searchFilmsDto.map { searchFilmMapper.mapDtoToEntity(it) })
        } catch (e: HttpException) {
            return State.Error(e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            State.Error("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getFilm(id: String): State<Film> {
        return try {
            val response = filmsApi.getFilm(id)

            if (!response.isSuccessful) {
                return State.Error("Request failed with status code: ${response.code()}")
            }

            val filmDto = response.body()
                ?: return State.Error("Response body is null")

            State.Success(filmMapper.mapDtoToEntity(filmDto))
        } catch (e: HttpException) {
            State.Error(e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            State.Error("Couldn't reach server. Check your internet connection")
        }
    }
}