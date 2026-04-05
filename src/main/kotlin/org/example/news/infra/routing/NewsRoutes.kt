package org.example.news.infra.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.middleware.withRole
import org.example.core.utils.saveToTempFile
import org.example.news.app.CreateNews
import org.example.news.app.GetAllNews
import org.example.news.app.dto.NewsResponse
import java.io.File

fun Application.newsRoutes(createNews: CreateNews, getAllNews: GetAllNews) {
    routing {
        authenticate("auth-jwt") {
            route("/api/v1/news") {

                // GET: Todos pueden ver las noticias
                get {
                    try {
                        val news = getAllNews.execute()
                        val response = news.map {
                            NewsResponse(it.id, it.title, it.content, it.publishDate, it.imageUrl)
                        }
                        call.respond(HttpStatusCode.OK, response)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener noticias"))
                    }
                }

                // POST: Solo el ADMIN puede publicar noticias
                withRole("ADMIN") {
                    post {
                        var title = ""
                        var content = ""
                        var imageFile: File? = null

                        try {
                            val multipartData = call.receiveMultipart()

                            multipartData.forEachPart { part ->
                                when (part) {
                                    is PartData.FormItem -> {
                                        when (part.name) {
                                            "title" -> title = part.value
                                            "content" -> content = part.value
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

                            val news = createNews.execute(title, content, imageFile)
                            val response = NewsResponse(news.id, news.title, news.content, news.publishDate, news.imageUrl)

                            call.respond(HttpStatusCode.Created, response)

                        } catch (e: Exception) {
                            imageFile?.delete()
                            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al procesar la noticia")))
                        }
                    }
                }
            }
        }
    }
}