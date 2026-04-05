package org.example.custom_lists.app

import org.example.custom_lists.domain.models.CustomList
import org.example.custom_lists.domain.repository.CustomListRepository

class GetRoomCustomLists(private val repository: CustomListRepository) {
    fun execute(roomId: Int): List<CustomList> {
        return repository.getByRoom(roomId)
    }
}