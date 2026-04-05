package org.example.news.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val id: Int,
    val title: String,
    val content: String,
    val publishDate: String,
    val imageUrl: String?
)