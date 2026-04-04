package org.example.virtual_rooms.app

import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class JoinVirtualRoom(private val repository: VirtualRoomRepository) {

    fun execute(invitationCode: String, currentUserId: Int): VirtualRoom {
        val room = repository.findByCode(invitationCode)
            ?: throw IllegalArgumentException("El código de invitación no existe")

        if (room.creatorId == currentUserId) {
            throw IllegalArgumentException("No puedes unirte a tu propia sala como invitado")
        }

        if (room.guestId != null) {
            throw IllegalArgumentException("Esta sala ya está llena")
        }

        return repository.updateGuest(room.id, currentUserId)
    }
}