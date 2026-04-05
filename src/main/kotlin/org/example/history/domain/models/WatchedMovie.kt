package org.example.history.domain.models

data class WatchedMovie(
    val historyId: Int,
    val movieId: Int,
    val title: String,
    val imageUrl: String?,
    val rating: Int,
    val watchedDate: String
)