package org.example.products.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
)