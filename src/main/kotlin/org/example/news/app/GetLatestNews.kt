package org.example.news.app

import org.example.news.domain.models.News
import org.example.news.domain.repository.NewsRepository

class GetLatestNews(private val repository: NewsRepository) {
    fun execute(): News? = repository.getLatest()
}