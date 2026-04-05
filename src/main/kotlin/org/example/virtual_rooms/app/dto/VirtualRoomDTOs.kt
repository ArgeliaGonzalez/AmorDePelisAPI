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
    val guestId: Int?
)

@Serializable
data class UpdateRoomRequest(val roomName: String)