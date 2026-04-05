package org.example.news.domain.repository

import org.example.news.domain.models.News

interface NewsRepository {
    fun create(title: String, content: String, imageUrl: String?): News
    fun getAll(): List<News>
}