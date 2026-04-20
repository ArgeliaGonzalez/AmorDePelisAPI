package org.example.movies.infra.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.middleware.withRole
import org.example.core.utils.saveToTempFile
import org.example.movies.app.CreateMovie
import org.example.movies.app.GetAllMovies
import org.example.movies.app.GetMovie
import org.example.movies.app.SearchMovies
import org.example.movies.app.dto.MovieResponse
import org.example.movies.domain.models.Movie
import org.example.tags.app.AddTagToMovie
import org.example.tags.app.GetMovieTags
import org.example.tags.app.dto.TagResponse
import java.io.File

fun Application.movieRoutes(
    createMovie: CreateMovie,
    getAllMovies: GetAllMovies,
    getMovie: GetMovie,
    searchMovies: SearchMovies,
    addTagToMovie: AddTagToMovie,
    getMovieTags: GetMovieTags
) {
    fun Movie.toResponse(tags: List<TagResponse> = emptyList()) = MovieResponse(
        id = id,
        title = title,
        synopsis = synopsis,
        durationMinutes = durationMinutes,
        imageUrl = imageUrl,
        tags = tags
    )

    routing {
        authenticate("auth-jwt") {
            route("/api/v1/movies") {

                get {
                    try {
                        val query = call.request.queryParameters["title"]
                        val movies = if (query.isNullOrBlank()) {
                            getAllMovies.execute()
                        } else {
                            searchMovies.execute(query)
                        }

                        val response = movies.map { movie ->
                            val tags = getMovieTags.execute(movie.id).map { TagResponse(it.id, it.name) }
                            movie.toResponse(tags)
                        }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al buscar"))
                    }
                }

                get("/{movieId}") {
                    try {
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw IllegalArgumentException("ID invalido")
                        val movie = getMovie.execute(movieId)
                        val tags = getMovieTags.execute(movie.id).map { TagResponse(it.id, it.name) }

                        call.respond(HttpStatusCode.OK, movie.toResponse(tags))
                    } catch (e: NoSuchElementException) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Pelicula no encontrada")))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener pelicula")))
                    }
                }

                withRole("ADMIN") {
                    post {
                        var title = ""
                        var synopsis: String? = null
                        var durationMinutes: Int? = null
                        var imageFile: File? = null
                        var tagsString: String? = null

                        try {
                            val multipartData = call.receiveMultipart()

                            multipartData.forEachPart { part ->
                                when (part) {
                                    is PartData.FormItem -> {
                                        when (part.name) {
                                            "title" -> title = part.value
                                            "synopsis" -> synopsis = part.value
                                            "durationMinutes" -> durationMinutes = part.value.toIntOrNull()
                                            "tags" -> tagsString = part.value
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

                            val tagIds = tagsString?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList()
                            tagIds.forEach { tagId ->
                                addTagToMovie.execute(movie.id, tagId)
                            }

                            val tags = getMovieTags.execute(movie.id).map { TagResponse(it.id, it.name) }
                            val response = movie.toResponse(tags)

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
}
