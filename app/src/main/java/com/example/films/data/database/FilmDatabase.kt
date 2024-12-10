package com.example.films.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FilmDbModel::class],
    version = 1
)
abstract class FilmDatabase:RoomDatabase() {
    abstract  fun getFilmDao(): FilmDao
}