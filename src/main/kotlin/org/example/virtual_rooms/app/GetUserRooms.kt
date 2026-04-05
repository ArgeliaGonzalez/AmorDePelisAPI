package org.example.virtual_rooms.app
import org.example.virtual_rooms.domain.models.VirtualRoom
import org.example.virtual_rooms.domain.repository.VirtualRoomRepository

class GetUserRooms(private val repository: VirtualRoomRepository) {
    fun execute(userId: Int): List<VirtualRoom> {
        return repository.findByUserId(userId)
    }
}