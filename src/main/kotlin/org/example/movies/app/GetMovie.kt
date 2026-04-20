package org.example.movies.app

import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository

class GetMovie(private val movieRepository: MovieRepository) {
    fun execute(id: Int): Movie {
        return movieRepository.findById(id) ?: throw NoSuchElementException("Pelicula no encontrada")
    }
}

