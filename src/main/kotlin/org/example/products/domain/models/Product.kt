package org.example.products.domain.models

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
)