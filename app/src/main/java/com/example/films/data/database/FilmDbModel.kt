package com.example.films.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films")
data class FilmDbModel(
    @PrimaryKey
    val id: String,
    val actors: String,
    val awards: String,
    val country: String,
    val imdbRating: String,
    val plot: String,
    val released: String,
    val runtime: String,
    val title: String,
    val writer: String,
    val poster: String
)