package com.example.films.domain.entities

data class Film(
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