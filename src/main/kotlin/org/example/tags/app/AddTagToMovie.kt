package org.example.tags.app
import org.example.tags.domain.repository.TagRepository

class AddTagToMovie(private val repository: TagRepository) {
    fun execute(movieId: Int, tagId: Int) = repository.addTagToMovie(movieId, tagId)
}