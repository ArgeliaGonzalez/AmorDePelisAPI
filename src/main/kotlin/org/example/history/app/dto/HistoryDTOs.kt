package org.example.history.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddHistoryRequest(
    val movieId: Int,
    val rating: Int
)

@Serializable
data class WatchedMovieResponse(
    val historyId: Int,
    val movieId: Int,
    val title: String,
    val imageUrl: String?,
    val rating: Int,
    val watchedDate: String
)