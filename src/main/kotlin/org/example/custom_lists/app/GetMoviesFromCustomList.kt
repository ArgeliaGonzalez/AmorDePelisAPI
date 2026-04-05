package org.example.custom_lists.app

import org.example.movies.domain.models.Movie
import org.example.custom_lists.domain.repository.CustomListRepository

class GetMoviesFromCustomList(private val repository: CustomListRepository) {
    fun execute(listId: Int): List<Movie> {
        return repository.getMovies(listId)
    }
}