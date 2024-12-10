package com.example.films.data.network.models.search


import com.google.gson.annotations.SerializedName

data class SearchFilmDto(
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Poster")
    val poster: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: String
)