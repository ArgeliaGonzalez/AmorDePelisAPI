package org.example.favorites.domain.repository

import org.example.movies.domain.models.Movie

interface FavoriteRepository {
    fun add(roomId: Int, movieId: Int)
    fun remove(roomId: Int, movieId: Int)
    fun exists(roomId: Int, movieId: Int): Boolean
    fun getByRoom(roomId: Int): List<Movie>
    fun countByRoom(roomId: Int): Int
}
