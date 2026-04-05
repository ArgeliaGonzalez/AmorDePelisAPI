package org.example.virtual_rooms.app
import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class UpdateVirtualRoom(private val repository: VirtualRoomRepository) {
    fun execute(roomId: Int, userId: Int, newName: String): VirtualRoom {
        if (newName.isBlank()) throw IllegalArgumentException("El nombre no puede estar vacío")

        val room = repository.findById(roomId) ?: throw Exception("Sala no encontrada")

        if (room.creatorId != userId) {
            throw IllegalAccessException("Solo el creador puede modificar esta sala")
        }

        return repository.updateName(roomId, newName)
    }
}