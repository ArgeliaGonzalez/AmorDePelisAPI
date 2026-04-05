package org.example.news.infra.persistence

import org.example.news.domain.models.News
import org.example.news.domain.repository.NewsRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class PostgresNewsRepository : NewsRepository {

    override fun create(title: String, content: String, imageUrl: String?): News {
        return transaction {
            val insertStmt = NewsTable.insert {
                it[titulo] = title
                it[contenido] = content
                it[fechaPublicacion] = LocalDate.now()
                it[imagenUrl] = imageUrl
            }
            News(
                id = insertStmt[NewsTable.id],
                title = title,
                content = content,
                publishDate = LocalDate.now().toString(),
                imageUrl = imageUrl
            )
        }
    }

    override fun getAll(): List<News> {
        return transaction {
            NewsTable.selectAll()
                .orderBy(NewsTable.fechaPublicacion, org.jetbrains.exposed.sql.SortOrder.DESC) // Las más nuevas primero
                .map {
                    News(
                        id = it[NewsTable.id],
                        title = it[NewsTable.titulo],
                        content = it[NewsTable.contenido],
                        publishDate = it[NewsTable.fechaPublicacion].toString(),
                        imageUrl = it[NewsTable.imagenUrl]
                    )
                }
        }
    }

    override fun getLatest(): News? {
        return transaction {
            NewsTable.selectAll()
                .orderBy(
                    NewsTable.fechaPublicacion to org.jetbrains.exposed.sql.SortOrder.DESC,
                    NewsTable.id to org.jetbrains.exposed.sql.SortOrder.DESC
                )
                .limit(1)
                .map {
                    News(
                        id = it[NewsTable.id],
                        title = it[NewsTable.titulo],
                        content = it[NewsTable.contenido],
                        publishDate = it[NewsTable.fechaPublicacion].toString(),
                        imageUrl = it[NewsTable.imagenUrl]
                    )
                }
                .singleOrNull()
        }
    }
}