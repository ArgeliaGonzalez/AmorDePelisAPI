package org.example.favorites.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.favorites.app.dto.CountResponse
import org.example.favorites.app.dto.FavoriteStateResponse
import org.example.favorites.domain.repository.FavoriteRepository
import org.example.movies.app.dto.MovieResponse
import org.example.movies.domain.models.Movie
import org.example.tags.app.GetMovieTags
import org.example.tags.app.dto.TagResponse

fun Application.favoriteRoutes(
    favoriteRepository: FavoriteRepository,
    getMovieTags: GetMovieTags
) {
    fun Movie.toFavoriteResponse() = MovieResponse(
        id = id,
        title = title,
        synopsis = synopsis,
        durationMinutes = durationMinutes,
        imageUrl = imageUrl,
        tags = getMovieTags.execute(id).map { TagResponse(it.id, it.name) },
        isFavorite = true
    )

    routing {
        authenticate("auth-jwt") {
            route("/api/v1/rooms/{roomId}/favorites") {
                get {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw IllegalArgumentException("ID de sala invalido")
                        val response = favoriteRepository.getByRoom(roomId).map { it.toFavoriteResponse() }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener favoritos"))
                    }
                }

                get("/count") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw IllegalArgumentException("ID de sala invalido")

                        call.respond(HttpStatusCode.OK, CountResponse(favoriteRepository.countByRoom(roomId)))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al contar favoritos")))
                    }
                }

                get("/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw IllegalArgumentException("ID de sala invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw IllegalArgumentException("ID de pelicula invalido")

                        call.respond(
                            HttpStatusCode.OK,
                            FavoriteStateResponse(movieId, favoriteRepository.exists(roomId, movieId))
                        )
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al consultar favorito")))
                    }
                }

                post("/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw IllegalArgumentException("ID de sala invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw IllegalArgumentException("ID de pelicula invalido")

                        favoriteRepository.add(roomId, movieId)
                        call.respond(HttpStatusCode.OK, FavoriteStateResponse(movieId, true))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "No se pudo agregar favorito")))
                    }
                }

                delete("/{movieId}") {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw IllegalArgumentException("ID de sala invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw IllegalArgumentException("ID de pelicula invalido")

                        favoriteRepository.remove(roomId, movieId)
                        call.respond(HttpStatusCode.OK, FavoriteStateResponse(movieId, false))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "No se pudo quitar favorito")))
                    }
                }
            }
        }
    }
}
