package org.example.custom_lists.domain.repository

import org.example.custom_lists.domain.models.CustomList
import org.example.movies.domain.models.Movie

interface CustomListRepository {
    fun create(roomId: Int, name: String, description: String?, colorHex: String?): CustomList
    fun getByRoom(roomId: Int): List<CustomList>
    fun getById(listId: Int): CustomList?
    fun countByRoom(roomId: Int): Int
    fun addMovie(listId: Int, movieId: Int)
    fun getMovies(listId: Int): List<Movie>
}
