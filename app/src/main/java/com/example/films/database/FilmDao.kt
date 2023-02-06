package com.example.films.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.films.models.film.Film

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFilm(film: Film)

    @Delete
    suspend fun deleteFilm(film: Film)

    @Query("select * from films")
    fun getSavedFilms(): LiveData<List<Film>>
}