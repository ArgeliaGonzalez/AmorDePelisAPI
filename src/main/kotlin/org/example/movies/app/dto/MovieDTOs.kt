package org.example.movies.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    val imageUrl: String?
)