package org.example.products.app

import org.example.products.domain.models.Product
import org.example.products.domain.repository.ProductRepository

class GetAllProducts(private val repository: ProductRepository) {
    fun execute(): List<Product> = repository.findAll()
}