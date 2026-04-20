package org.example.favorites.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountResponse(
    val count: Int
)

@Serializable
data class FavoriteStateResponse(
    val movieId: Int,
    val isFavorite: Boolean
)
