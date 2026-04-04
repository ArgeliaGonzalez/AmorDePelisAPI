package org.example.movies.infra

import io.ktor.server.application.*
import org.example.movies.app.CreateMovie
import org.example.movies.infra.persistence.PostgresMovieRepository
import org.example.movies.infra.routing.movieRoutes

fun Application.initMoviesModule() {
    val repository = PostgresMovieRepository()
    val createMovie = CreateMovie(repository)

    movieRoutes(createMovie)
}