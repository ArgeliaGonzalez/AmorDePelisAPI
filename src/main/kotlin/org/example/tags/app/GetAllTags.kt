package org.example.tags.app
import org.example.tags.domain.models.Tag
import org.example.tags.domain.repository.TagRepository

class GetAllTags(private val repository: TagRepository) {
    fun execute(): List<Tag> = repository.getAll()
}