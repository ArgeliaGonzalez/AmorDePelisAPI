package org.example.movies.app

import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository

class GetAllMovies(private val movieRepository: MovieRepository) {
    fun execute(): List<Movie> {
        return movieRepository.findAll()
    }
}