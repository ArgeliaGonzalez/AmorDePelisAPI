package org.example.tags.infra.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.middleware.withRole
import org.example.tags.app.*
import org.example.tags.app.dto.*

fun Application.tagRoutes(
    createTag: CreateTag,
    getAllTags: GetAllTags,
    addTagToMovie: AddTagToMovie,
    getMovieTags: GetMovieTags
) {
    routing {
        authenticate("auth-jwt") {

            route("/api/v1/tags") {
                get {
                    val tags = getAllTags.execute()
                    call.respond(HttpStatusCode.OK, tags.map { TagResponse(it.id, it.name) })
                }

                withRole("ADMIN") {
                    post {
                        try {
                            val request = call.receive<CreateTagRequest>()
                            val tag = createTag.execute(request.name)
                            call.respond(HttpStatusCode.Created, TagResponse(tag.id, tag.name))
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No se pudo crear la etiqueta"))
                        }
                    }
                }
            }

            route("/api/v1/movies/{movieId}/tags") {
                get {
                    try {
                        val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception()
                        val tags = getMovieTags.execute(movieId)
                        call.respond(HttpStatusCode.OK, tags.map { TagResponse(it.id, it.name) })
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener etiquetas"))
                    }
                }

                withRole("ADMIN") {
                    post("/{tagId}") {
                        try {
                            val movieId = call.parameters["movieId"]?.toInt() ?: throw Exception()
                            val tagId = call.parameters["tagId"]?.toInt() ?: throw Exception()
                            addTagToMovie.execute(movieId, tagId)
                            call.respond(HttpStatusCode.OK, mapOf("message" to "Etiqueta asignada a la película"))
                        } catch (e: Exception) {
                            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Error al asignar etiqueta"))
                        }
                    }
                }
            }
        }
    }
}