package org.example.products.infra.persistence

import org.example.products.domain.models.Product
import org.example.products.domain.repository.ProductRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresProductRepository : ProductRepository {
    override fun save(product: Product): Product {
        return transaction {
            val insertStmt = ProductsTable.insert {
                it[nombre] = product.name
                it[descripcion] = product.description
                it[imagenUrl] = product.imageUrl
            }
            product.copy(id = insertStmt[ProductsTable.id])
        }
    }

    override fun findAll(): List<Product> {
        return transaction {
            ProductsTable.selectAll().map {
                Product(
                    id = it[ProductsTable.id],
                    name = it[ProductsTable.nombre],
                    description = it[ProductsTable.descripcion],
                    imageUrl = it[ProductsTable.imagenUrl]
                )
            }
        }
    }
}