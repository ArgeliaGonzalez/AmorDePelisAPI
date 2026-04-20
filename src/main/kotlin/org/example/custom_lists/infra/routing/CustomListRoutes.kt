package org.example.custom_lists.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.custom_lists.app.AddMovieToCustomList
import org.example.custom_lists.app.CreateCustomList
import org.example.custom_lists.app.GetCustomList
import org.example.custom_lists.app.GetMoviesFromCustomList
import org.example.custom_lists.app.GetRoomCustomLists
import org.example.custom_lists.app.dto.CreateListRequest
import org.example.custom_lists.app.dto.CustomListResponse
import org.example.custom_lists.domain.models.CustomList
import org.example.movies.app.dto.MovieResponse
import org.example.movies.domain.models.Movie

fun Application.customListRoutes(
    createCustomList: CreateCustomList,
    getRoomCustomLists: GetRoomCustomLists,
    getCustomList: GetCustomList,
    addMovieToCustomList: AddMovieToCustomList,
    getMoviesFromCustomList: GetMoviesFromCustomList
) {
    fun Movie.toResponse() = MovieResponse(
        id = id,
        title = title,
        synopsis = synopsis,
        durationMinutes = durationMinutes,
        imageUrl = imageUrl
    )

    fun CustomList.toResponse(movies: List<Movie> = emptyList()) = CustomListResponse(
        id = id,
        name = name,
        description = description.orEmpty(),
        colorHex = colorHex,
        movieCount = if (movieCount > 0) movieCount else movies.size,
        movies = movies.map { it.toResponse() }
    )

    routing {
        authenticate("auth-jwt") {
            route("/api/v1/rooms/{roomId}/lists") {

                post {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID invalido")
                        val request = call.receive<CreateListRequest>()
                        val list = createCustomList.execute(
                            roomId = roomId,
                            name = request.name,
                            description = request.description,
                            colorHex = request.colorHex
                        )

                        call.respond(HttpStatusCode.Created, list.toResponse())
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al crear lista")))
                    }
                }

                get {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID invalido")
                        val lists = getRoomCustomLists.execute(roomId)
                        val response = lists.map { list ->
                            list.toResponse(getMoviesFromCustomList.execute(list.id))
                        }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener listas"))
                    }
                }

                get("/{listId}") {
                    try {
                        val listId = call.parameters["listId"]?.toInt() ?: throw Exception("ID de lista invalido")
                        val list = getCustomList.execute(listId)
                        val movies = getMoviesFromCustomList.execute(listId)

                        call.respond(HttpStatusCode.OK, list.toResponse(movies))
                    } catch (e: NoSuchElementException) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "Lista no encontrada")))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener lista")))
                    }
                }

                post("/{listId}/movies/{movieId}") {
                    try {
                        val listId = call.parameters["listId"]?.toInt() ?: throw Exception("ID de lista invalido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception("ID de pelicula invalido")

                        addMovieToCustomList.execute(listId, movieId)

                        call.respond(HttpStatusCode.OK, mapOf("message" to "Pelicula agregada a la lista"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No se pudo agregar a la lista"))
                    }
                }

                get("/{listId}/movies") {
                    try {
                        val listId = call.parameters["listId"]?.toInt() ?: throw Exception("ID de lista invalido")
                        val response = getMoviesFromCustomList.execute(listId).map { it.toResponse() }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener peliculas"))
                    }
                }
            }
        }
    }
}
