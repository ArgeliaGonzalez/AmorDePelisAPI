package org.example.virtual_rooms.app
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class DeleteVirtualRoom(private val repository: VirtualRoomRepository) {
    fun execute(roomId: Int, userId: Int) {
        val room = repository.findById(roomId) ?: throw Exception("Sala no encontrada")

        if (room.creatorId != userId) {
            throw IllegalAccessException("Solo el creador puede eliminar esta sala")
        }

        repository.delete(roomId)
    }
}