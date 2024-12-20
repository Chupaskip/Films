package com.example.films.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.films.data.database.FilmDao
import com.example.films.data.mappers.FilmMapper
import com.example.films.data.network.FilmsApi
import com.example.films.domain.entities.Film
import com.example.films.domain.repository.FilmRepository
import com.example.films.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val filmDao: FilmDao,
    private val filmsApi: FilmsApi
) : FilmRepository {

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

    override suspend fun getSearchFilms(filmName: String): Resource<List<Film>> {
        return try {
            val response = filmsApi.getSearchFilms(filmName)

            if (!response.isSuccessful) {
                return Resource.Error("Request failed with status code: ${response.code()}")
            }

            val searchFilmsDto = filmsApi.getSearchFilms(filmName).body()?.films
                ?: return Resource.Error("Response body is null")

            Resource.Success(searchFilmsDto.map { filmMapper.mapSearchDtoToFilmEntity(it) })
        } catch (e: HttpException) {
            return Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getFilm(id: String): Resource<Film> {
        return try {
            val response = filmsApi.getFilm(id)

            if (!response.isSuccessful) {
                return Resource.Error("Request failed with status code: ${response.code()}")
            }

            val filmDto = response.body()
                ?: return Resource.Error("Response body is null")

            Resource.Success(filmMapper.mapDtoToEntity(filmDto))
        } catch (e: HttpException) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your internet connection")
        }
    }
}