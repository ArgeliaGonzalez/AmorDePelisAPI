package org.example.movies.domain.models

data class Movie(
    val id: Int = 0,
    val title: String,
    val synopsis: String?,
    val durationMinutes: Int?,
    val imageUrl: String?
)