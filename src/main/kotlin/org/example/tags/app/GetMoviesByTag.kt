package org.example.tags.app

import org.example.movies.domain.models.Movie
import org.example.tags.domain.repository.TagRepository

class GetMoviesByTag(private val repository: TagRepository) {
    fun execute(tagId: Int): List<Movie> = repository.getMoviesByTag(tagId)
}