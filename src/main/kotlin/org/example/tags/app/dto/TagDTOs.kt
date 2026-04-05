package org.example.tags.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTagRequest(val name: String)

@Serializable
data class TagResponse(val id: Int, val name: String)