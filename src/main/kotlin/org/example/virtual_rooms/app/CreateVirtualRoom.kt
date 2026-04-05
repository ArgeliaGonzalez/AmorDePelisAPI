package org.example.virtual_rooms.app

import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class CreateVirtualRoom(private val repository: VirtualRoomRepository) {

    fun execute(roomName: String, creatorId: Int): VirtualRoom {
        if (roomName.isBlank()) throw IllegalArgumentException("El nombre de la sala no puede estar vacío")

        val code = generateRandomCode(6)

        val newRoom = VirtualRoom(
            roomName = roomName,
            invitationCode = code,
            creatorId = creatorId
        )

        return repository.save(newRoom)
    }

    private fun generateRandomCode(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}