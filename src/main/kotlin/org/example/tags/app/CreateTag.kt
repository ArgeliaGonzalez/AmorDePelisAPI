package org.example.tags.app
import org.example.tags.domain.models.Tag
import org.example.tags.domain.repository.TagRepository

class CreateTag(private val repository: TagRepository) {
    fun execute(name: String): Tag {
        if (name.isBlank()) throw IllegalArgumentException("El nombre no puede estar vacío")
        return repository.create(name)
    }
}