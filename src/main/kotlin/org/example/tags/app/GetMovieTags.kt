package org.example.tags.app
import org.example.tags.domain.models.Tag
import org.example.tags.domain.repository.TagRepository

class GetMovieTags(private val repository: TagRepository) {
    fun execute(movieId: Int): List<Tag> = repository.getTagsByMovie(movieId)
}