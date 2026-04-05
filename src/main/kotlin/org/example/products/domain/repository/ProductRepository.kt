package org.example.products.domain.repository

import org.example.products.domain.models.Product

interface ProductRepository {
    fun save(product: Product): Product
    fun findAll(): List<Product>
}