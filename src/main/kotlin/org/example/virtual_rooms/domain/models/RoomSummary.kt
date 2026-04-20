package org.example.virtual_rooms.domain.models

data class RoomSummary(
    val roomId: Int,
    val roomName: String,
    val currentUserName: String,
    val partnerName: String?,
    val listsCount: Int,
    val favoritesCount: Int,
    val ratingsCount: Int,
    val averageRating: Double
)
