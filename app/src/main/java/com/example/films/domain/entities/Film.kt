package com.example.films.domain.entities

data class Film(
    val id: String,
    val actors: String? = null,
    val awards: String? = null,
    val country: String? = null,
    val imdbRating: String? = null,
    val plot: String? = null,
    val released: String? = null,
    val runtime: String? = null,
    val title: String,
    val writer: String? = null,
    val poster: String
)