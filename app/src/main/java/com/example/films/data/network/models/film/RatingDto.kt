package com.example.films.data.network.models.film


import com.google.gson.annotations.SerializedName

data class RatingDto(
    @SerializedName("Source")
    val source: String,
    @SerializedName("Value")
    val value: String
)