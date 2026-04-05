package org.example.custom_lists.app

import org.example.custom_lists.domain.repository.CustomListRepository

class AddMovieToCustomList(private val repository: CustomListRepository) {
    fun execute(listId: Int, movieId: Int) {
        repository.addMovie(listId, movieId)
    }
}