package org.example.custom_lists.domain.models

data class CustomList(
    val id: Int,
    val roomId: Int,
    val name: String,
    val description: String? = null,
    val colorHex: String = "#E91E63",
    val movieCount: Int = 0
)
