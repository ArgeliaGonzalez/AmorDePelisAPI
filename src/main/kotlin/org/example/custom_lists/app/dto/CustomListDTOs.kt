package org.example.custom_lists.app.dto

import kotlinx.serialization.Serializable
import org.example.movies.app.dto.MovieResponse

@Serializable
data class CreateListRequest(
    val name: String,
    val description: String? = null,
    val colorHex: String? = null
)

@Serializable
data class CustomListResponse(
    val id: Int,
    val name: String,
    val description: String = "",
    val colorHex: String = "#E91E63",
    val movieCount: Int = 0,
    val movies: List<MovieResponse> = emptyList()
)
