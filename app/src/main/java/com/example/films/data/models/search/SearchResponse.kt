package com.example.films.data.models.search


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val films: List<Search>,
    val totalResults: String
)