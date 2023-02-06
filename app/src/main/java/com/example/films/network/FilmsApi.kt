package com.example.films.network

import com.example.films.models.film.Film
import com.example.films.models.search.SearchResponse
import com.example.films.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsApi {


    @GET("/")
    suspend fun getSearchFilms(
        @Query("s")
        search: String,
        @Query("apikey")
        apikey: String = API_KEY
    ): Response<SearchResponse>

    @GET("/")
    suspend fun getFilm(
        @Query("i")
        id: String,
        @Query("apikey")
        apikey: String = API_KEY,
        @Query("plot")
        plot: String = "full"
    ): Response<Film>
}


