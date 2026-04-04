package org.example.movies.infra.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.middleware.withRole
import org.example.core.utils.saveToTempFile
import org.example.movies.app.CreateMovie
import org.example.movies.app.dto.MovieResponse
import java.io.File

fun Application.movieRoutes(createMovie: CreateMovie) {
    routing {
        route("/api/v1/movies") {

            withRole("ADMIN") {
                post {
                    var title = ""
                    var synopsis: String? = null
                    var durationMinutes: Int? = null
                    var imageFile: File? = null

                    try {
                        val multipartData = call.receiveMultipart()

                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    when (part.name) {
                                        "title" -> title = part.value
                                        "synopsis" -> synopsis = part.value
                                        "durationMinutes" -> durationMinutes = part.value.toIntOrNull()
                                    }
                                }
                                is PartData.FileItem -> {
                                    if (part.name == "image") {
                                        imageFile = part.saveToTempFile()
                                    }
                                }
                                else -> {}
                            }
                            part.dispose()
                        }

                        val movie = createMovie.execute(title, synopsis, durationMinutes, imageFile)
                        val response = MovieResponse(
                            id = movie.id,
                            title = movie.title,
                            imageUrl = movie.imageUrl
                        )
                        call.respond(HttpStatusCode.Created, response)

                    } catch (e: Exception) {
                        imageFile?.delete()
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al procesar la película")))
                    }
                }
            }
        }
    }
}