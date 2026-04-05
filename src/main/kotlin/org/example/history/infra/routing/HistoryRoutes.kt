package org.example.history.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.history.app.AddMovieToHistory
import org.example.history.app.GetRoomHistory
import org.example.history.app.dto.AddHistoryRequest
import org.example.history.app.dto.WatchedMovieResponse

fun Application.historyRoutes(
    addMovieToHistory: AddMovieToHistory,
    getRoomHistory: GetRoomHistory
) {
    routing {
        authenticate("auth-jwt") {
            route("/api/v1/rooms/{roomId}/history") {

                post {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID de sala inválido")
                        val request = call.receive<AddHistoryRequest>()

                        addMovieToHistory.execute(roomId, request.movieId, request.rating)
                        call.respond(HttpStatusCode.Created, mapOf("message" to "Película registrada en el historial"))

                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Datos inválidos")))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al registrar película"))
                    }
                }

                get {
                    try {
                        val roomId = call.parameters["roomId"]?.toInt() ?: throw Exception("ID de sala inválido")
                        val history = getRoomHistory.execute(roomId)

                        val response = history.map {
                            WatchedMovieResponse(it.historyId, it.movieId, it.title, it.imageUrl, it.rating, it.watchedDate)
                        }

                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener historial"))
                    }
                }
            }
        }
    }
}