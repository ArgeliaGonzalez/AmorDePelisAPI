package org.example.news.domain.models

data class News(
    val id: Int,
    val title: String,
    val content: String,
    val publishDate: String,
    val imageUrl: String?
)