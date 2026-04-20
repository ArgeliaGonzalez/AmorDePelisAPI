package org.example.virtual_rooms.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(val roomName: String)

@Serializable
data class JoinRoomRequest(val invitationCode: String)

@Serializable
data class RoomResponse(
    val id: Int,
    val roomName: String,
    val invitationCode: String,
    val creatorId: Int,
    val guestId: Int?,
    val creatorName: String? = null,
    val guestName: String? = null,
    val currentUserName: String? = null,
    val partnerName: String? = null
)

@Serializable
data class UpdateRoomRequest(val roomName: String)

@Serializable
data class RoomSummaryResponse(
    val roomId: Int,
    val roomName: String,
    val currentUserName: String,
    val partnerName: String?,
    val listsCount: Int,
    val favoritesCount: Int,
    val ratingsCount: Int,
    val averageRating: Double
)
