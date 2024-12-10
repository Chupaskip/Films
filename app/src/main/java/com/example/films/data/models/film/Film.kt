package com.example.films.data.models.film


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.films.data.models.search.Search
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "films")
data class Film(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("imdbID")
    val id: String = "",
    @SerializedName("Actors")
    val actors: String = "",
    @SerializedName("Awards")
    val awards: String = "",
    @SerializedName("Country")
    val country: String = "",
    @SerializedName("Director")
    val director: String = "",
    @SerializedName("Genre")
    val genre: String = "",
    val imdbRating: String = "",
    val imdbVotes: String = "",
    @SerializedName("Language")
    val language: String = "",
    @SerializedName("Metascore")
    val metascore: String = "",
    @SerializedName("Plot")
    val plot: String = "",
    @SerializedName("Poster")
    val poster: String = "",
    @SerializedName("Rated")
    val rated: String = "",
//    @SerializedName("Ratings")
//    val ratings: List<Rating> = listOf(),
    @SerializedName("Released")
    val released: String = "",
    @SerializedName("Response")
    val response: String = "",
    @SerializedName("Runtime")
    val runtime: String = "",
    @SerializedName("Title")
    val title: String = "",
    val totalSeasons: String = "",
    @SerializedName("Type")
    val type: String = "",
    @SerializedName("Writer")
    val writer: String = "",
    @SerializedName("Year")
    val year: String = ""
): Serializable

fun Film.toSearch() = Search(
    id = this.id,
    image = this.poster,
    title = this.title,
    type = this.type,
    year = this.year
)