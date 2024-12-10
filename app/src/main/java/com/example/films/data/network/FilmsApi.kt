package com.example.films.data.network

import com.example.films.data.network.RetrofitInstance.Companion.API_KEY
import com.example.films.data.network.models.film.FilmDto
import com.example.films.data.network.models.search.SearchListDto
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
    ): Response<SearchListDto>

    @GET("/")
    suspend fun getFilm(
        @Query("i")
        id: String,
        @Query("apikey")
        apikey: String = API_KEY,
        @Query("plot")
        plot: String = "full"
    ): Response<FilmDto>
}


