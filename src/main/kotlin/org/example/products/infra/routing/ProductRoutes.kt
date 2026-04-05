package org.example.products.infra.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.core.middleware.withRole
import org.example.core.utils.saveToTempFile
import org.example.products.app.CreateProduct
import org.example.products.app.GetAllProducts
import org.example.products.app.dto.ProductResponse
import java.io.File

fun Application.productRoutes(createProduct: CreateProduct, getAllProducts: GetAllProducts) {
    routing {
        authenticate("auth-jwt") {
            route("/api/v1/products") {
                get {
                    val products = getAllProducts.execute()
                    call.respond(HttpStatusCode.OK, products.map { ProductResponse(it.id, it.name, it.description, it.imageUrl) })
                }

                withRole("ADMIN") {
                    post {
                        var name = ""
                        var description: String? = null
                        var imageFile: File? = null

                        val multipartData = call.receiveMultipart()
                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    when (part.name) {
                                        "name" -> name = part.value
                                        "description" -> description = part.value
                                    }
                                }
                                is PartData.FileItem -> {
                                    if (part.name == "image") imageFile = part.saveToTempFile()
                                }
                                else -> {}
                            }
                            part.dispose()
                        }

                        val product = createProduct.execute(name, description, imageFile)
                        call.respond(HttpStatusCode.Created, ProductResponse(product.id, product.name, product.description, product.imageUrl))
                    }
                }
            }
        }
    }
}