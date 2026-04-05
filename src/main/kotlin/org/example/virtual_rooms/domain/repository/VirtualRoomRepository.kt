package org.example.virtual_rooms.domain.repository

import org.example.virtual_rooms.domain.models.VirtualRoom

interface VirtualRoomRepository {
    fun save(room: VirtualRoom): VirtualRoom
    fun findByCode(code: String): VirtualRoom?
    fun updateGuest(roomId: Int, guestId: Int): VirtualRoom
    fun findById(id: Int): VirtualRoom?
    fun findByUserId(userId: Int): List<VirtualRoom>
    fun updateName(roomId: Int, newName: String): VirtualRoom
    fun delete(roomId: Int)
}