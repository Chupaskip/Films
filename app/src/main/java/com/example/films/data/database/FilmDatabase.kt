package com.example.films.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FilmDbModel::class],
    version = 3
)
abstract class FilmDatabase:RoomDatabase() {
    abstract  fun getFilmDao(): FilmDao
}