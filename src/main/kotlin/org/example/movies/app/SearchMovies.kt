package org.example.movies.app

import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository

class SearchMovies(private val movieRepository: MovieRepository) {
    fun execute(query: String): List<Movie> {
        return movieRepository.searchByTitle(query)
    }
}