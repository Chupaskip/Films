package com.example.films.data.mappers

import com.example.films.data.network.models.search.SearchFilmDto
import com.example.films.domain.entities.SearchFilm

class SearchFilmMapper {

    fun mapDtoToEntity(searchFilmDto: SearchFilmDto) =
        SearchFilm(
            id = searchFilmDto.id,
            poster = searchFilmDto.poster,
            title = searchFilmDto.title
        )
}