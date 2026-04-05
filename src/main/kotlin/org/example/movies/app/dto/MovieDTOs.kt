package org.example.movies.app.dto

import kotlinx.serialization.Serializable
import org.example.tags.app.dto.TagResponse

@Serializable
data class MovieResponse(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val tags: List<TagResponse> = emptyList()
)