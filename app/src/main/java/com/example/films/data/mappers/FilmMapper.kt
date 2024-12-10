package com.example.films.data.mappers

import com.example.films.data.database.FilmDbModel
import com.example.films.data.network.models.film.FilmDto
import com.example.films.domain.entities.Film

class FilmMapper {

    fun mapDtoToDbModel(filmDto: FilmDto) =
        FilmDbModel(
            id = filmDto.id,
            actors = filmDto.actors,
            awards = filmDto.awards,
            country = filmDto.country,
            imdbRating = filmDto.imdbRating,
            plot = filmDto.plot,
            released = filmDto.released,
            runtime = filmDto.runtime,
            title = filmDto.title,
            writer = filmDto.writer,
            poster = filmDto.poster
        )

    fun mapDtoToEntity(filmDto: FilmDto) =
        Film(
            id = filmDto.id,
            actors = filmDto.actors,
            awards = filmDto.awards,
            country = filmDto.country,
            imdbRating = filmDto.imdbRating,
            plot = filmDto.plot,
            released = filmDto.released,
            runtime = filmDto.runtime,
            title = filmDto.title,
            writer = filmDto.writer,
            poster = filmDto.poster
        )

    fun mapDbModelToEntity(filmDbModel: FilmDbModel) =
        Film(
            id = filmDbModel.id,
            actors = filmDbModel.actors,
            awards = filmDbModel.awards,
            country = filmDbModel.country,
            imdbRating = filmDbModel.imdbRating,
            plot = filmDbModel.plot,
            released = filmDbModel.released,
            runtime = filmDbModel.runtime,
            title = filmDbModel.title,
            writer = filmDbModel.writer,
            poster = filmDbModel.poster
        )

    fun mapEntityToDbModel(film: Film) =
        FilmDbModel(
            id = film.id,
            actors = film.actors,
            awards = film.awards,
            country = film.country,
            imdbRating = film.imdbRating,
            plot = film.plot,
            released = film.released,
            runtime = film.runtime,
            title = film.title,
            writer = film.writer,
            poster = film.poster
        )
}