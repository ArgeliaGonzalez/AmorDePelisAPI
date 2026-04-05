package org.example.news.infra

import io.ktor.server.application.*
import org.example.news.app.CreateNews
import org.example.news.app.GetAllNews
import org.example.news.app.GetLatestNews
import org.example.news.infra.persistence.PostgresNewsRepository
import org.example.news.infra.routing.newsRoutes

fun Application.initNewsModule() {
    val repository = PostgresNewsRepository()

    val createNews = CreateNews(repository)
    val getAllNews = GetAllNews(repository)
    val getLatestNews = GetLatestNews(repository)

    newsRoutes(createNews, getAllNews, getLatestNews)
}