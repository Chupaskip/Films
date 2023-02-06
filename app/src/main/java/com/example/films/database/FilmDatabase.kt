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

    companion object{
        @Volatile
        private var instance:FilmDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FilmDatabase::class.java,
                "film_db.db"
            ).build()
    }
}