package com.example.films.data.network.models.search


import com.google.gson.annotations.SerializedName

data class SearchListDto(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val films: List<SearchFilmDto>,
    val totalResults: String
)