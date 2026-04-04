package org.example.virtual_rooms.domain.models

data class VirtualRoom(
    val id: Int = 0,
    val roomName: String,
    val invitationCode: String,
    val creatorId: Int,
    val guestId: Int? = null
)