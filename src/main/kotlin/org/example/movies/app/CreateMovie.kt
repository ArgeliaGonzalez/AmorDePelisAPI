package org.example.movies.app

import org.example.core.external.CloudinaryService
import org.example.movies.domain.models.Movie
import org.example.movies.domain.repository.MovieRepository
import java.io.File

class CreateMovie(private val movieRepository: MovieRepository) {

    fun execute(title: String, synopsis: String?, durationMinutes: Int?, imageFile: File?): Movie {
        if (title.isBlank()) throw IllegalArgumentException("El título de la película es obligatorio")

        var imageUrl: String? = null

        if (imageFile != null) {
            imageUrl = CloudinaryService.uploadImage(imageFile, "portadas")
            imageFile.delete()
        }

        val newMovie = Movie(
            title = title,
            synopsis = synopsis,
            durationMinutes = durationMinutes,
            imageUrl = imageUrl
        )

        return movieRepository.save(newMovie)
    }
}