package org.example.news.app

import org.example.news.domain.models.News
import org.example.news.domain.repository.NewsRepository

class GetAllNews(private val repository: NewsRepository) {
    fun execute(): List<News> = repository.getAll()
}