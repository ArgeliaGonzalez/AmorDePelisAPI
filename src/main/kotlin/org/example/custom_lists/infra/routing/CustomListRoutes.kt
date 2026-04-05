package org.example.custom_lists.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.custom_lists.app.CreateCustomList
import org.example.custom_lists.app.GetRoomCustomLists
import org.example.custom_lists.app.dto.CreateListRequest
import org.example.custom_lists.app.dto.CustomListResponse
import org.example.movies.app.dto.MovieResponse
import org.example.custom_lists.app.AddMovieToCustomList
import org.example.custom_lists.app.GetMoviesFromCustomList

fun Application.customListRoutes(
    createCustomList: CreateCustomList,
    getRoomCustomLists: GetRoomCustomLists,
    addMovieToCustomList: AddMovieToCustomList,
    getMoviesFromCustomList: GetMoviesFromCustomList
) {
    routing {
        authenticate("auth-jwt") {
            route("/api/v1/rooms/{roomId}/lists") {

                // POST: Crear una nueva lista
                post {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID inválido")
                        val request = call.receive<CreateListRequest>()

                        val list = createCustomList.execute(roomId, request.name)

                        call.respond(HttpStatusCode.Created, CustomListResponse(list.id, list.name))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al crear lista")))
                    }
                }

                // GET: Obtener las listas de la sala
                get {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID inválido")
                        val lists = getRoomCustomLists.execute(roomId)

                        val response = lists.map { CustomListResponse(it.id, it.name) }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener listas"))
                    }
                }

                post("/{listId}/movies/{movieId}") {
                    try {
                        val listId = call.parameters["listId"]?.toInt() ?: throw Exception("ID de lista inválido")
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception("ID de película inválido")

                        addMovieToCustomList.execute(listId, movieId)

                        call.respond(HttpStatusCode.OK, mapOf("message" to "Película añadida a la lista"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No se pudo añadir a la lista"))
                    }
                }

                // GET: Obtener películas de una lista
                get("/{listId}/movies") {
                    try {
                        val listId = call.parameters["listId"]?.toInt() ?: throw Exception("ID de lista inválido")
                        val movies = getMoviesFromCustomList.execute(listId)

                        val response = movies.map {
                            MovieResponse(it.id, it.title, it.imageUrl)
                        }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener películas"))
                    }
                }
            }
        }
    }
}