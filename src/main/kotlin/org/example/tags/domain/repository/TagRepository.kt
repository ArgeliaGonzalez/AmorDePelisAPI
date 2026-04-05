package org.example.tags.domain.repository

import org.example.tags.domain.models.Tag
import org.example.movies.domain.models.Movie

interface TagRepository {
    fun create(name: String): Tag
    fun getAll(): List<Tag>
    fun addTagToMovie(movieId: Int, tagId: Int)
    fun getTagsByMovie(movieId: Int): List<Tag>
    fun getMoviesByTag(tagId: Int): List<Movie>
}