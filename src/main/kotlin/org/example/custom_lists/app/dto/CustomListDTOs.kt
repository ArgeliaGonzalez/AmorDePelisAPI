package org.example.custom_lists.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateListRequest(
    val name: String
)

@Serializable
data class CustomListResponse(
    val id: Int,
    val name: String
)