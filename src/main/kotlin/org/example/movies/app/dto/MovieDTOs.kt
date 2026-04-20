package org.example.movies.app.dto

import kotlinx.serialization.Serializable
import org.example.tags.app.dto.TagResponse

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    val synopsis: String? = null,
    val durationMinutes: Int? = null,
    val imageUrl: String?,
    val tags: List<TagResponse> = emptyList(),
    val averageRating: Double? = null,
    val ratingCount: Int = 0,
    val isFavorite: Boolean = false
)
