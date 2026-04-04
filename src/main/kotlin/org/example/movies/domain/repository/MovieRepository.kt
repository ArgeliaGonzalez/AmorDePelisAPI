package org.example.movies.domain.repository

import org.example.movies.domain.models.Movie

interface MovieRepository {
    fun save(movie: Movie): Movie
}