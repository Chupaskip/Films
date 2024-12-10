package com.example.films.data.models.search


import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Poster")
    val image: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: String
)