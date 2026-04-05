package org.example.news.app

import org.example.core.external.CloudinaryService
import org.example.news.domain.models.News
import org.example.news.domain.repository.NewsRepository
import java.io.File

class CreateNews(private val repository: NewsRepository) {
    fun execute(title: String, content: String, imageFile: File?): News {
        if (title.isBlank() || content.isBlank()) {
            throw IllegalArgumentException("El título y el contenido son obligatorios")
        }

        var imageUrl: String? = null

        if (imageFile != null) {
            imageUrl = CloudinaryService.uploadImage(imageFile, "noticias")
            imageFile.delete()
        }

        return repository.create(title, content, imageUrl)
    }
}