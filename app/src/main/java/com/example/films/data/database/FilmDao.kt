package com.example.films.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFilm(film: FilmDbModel)

    @Delete
    suspend fun deleteFilm(film: FilmDbModel)

    @Query("select * from favorite_films")
    fun getSavedFilms(): LiveData<List<FilmDbModel>>
}