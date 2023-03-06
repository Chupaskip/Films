package com.example.films.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.films.models.film.Film

@Database(
    entities = [Film::class],
    version = 1
)
abstract class FilmDatabase:RoomDatabase() {
    abstract  fun getFilmDao():FilmDao
}