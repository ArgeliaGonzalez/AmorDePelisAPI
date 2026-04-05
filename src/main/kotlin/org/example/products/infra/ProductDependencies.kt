package org.example.products.infra

import io.ktor.server.application.*
import org.example.products.app.CreateProduct
import org.example.products.app.GetAllProducts
import org.example.products.infra.persistence.PostgresProductRepository
import org.example.products.infra.routing.productRoutes

fun Application.initProductsModule() {
    val repository = PostgresProductRepository()
    productRoutes(CreateProduct(repository), GetAllProducts(repository))
}