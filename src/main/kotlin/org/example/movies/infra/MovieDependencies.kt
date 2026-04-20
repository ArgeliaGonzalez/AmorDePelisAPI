package org.example.movies.infra

import io.ktor.server.application.*
import org.example.movies.app.CreateMovie
import org.example.movies.app.GetAllMovies
import org.example.movies.app.GetMovie
import org.example.movies.app.SearchMovies
import org.example.movies.infra.persistence.PostgresMovieRepository
import org.example.movies.infra.routing.movieRoutes
import org.example.tags.infra.persistence.PostgresTagRepository
import org.example.tags.app.AddTagToMovie
import org.example.tags.app.GetMovieTags

fun Application.initMoviesModule() {
    val repository = PostgresMovieRepository()
    val tagRepository = PostgresTagRepository()

    val createMovie = CreateMovie(repository)
    val getAllMovies = GetAllMovies(repository)
    val getMovie = GetMovie(repository)
    val searchMovies = SearchMovies(repository)
    val addTagToMovie = AddTagToMovie(tagRepository)
    val getMovieTags = GetMovieTags(tagRepository)

    movieRoutes(createMovie, getAllMovies, getMovie, searchMovies, addTagToMovie, getMovieTags)
}
