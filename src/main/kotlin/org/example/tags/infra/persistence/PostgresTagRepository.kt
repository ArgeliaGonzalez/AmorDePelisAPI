package org.example.tags.infra.persistence

import org.example.tags.domain.models.Tag
import org.example.tags.domain.repository.TagRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresTagRepository : TagRepository {

    override fun create(name: String): Tag {
        return transaction {
            val insertStmt = TagsTable.insert {
                it[nombre] = name
            }
            Tag(id = insertStmt[TagsTable.id], name = name)
        }
    }

    override fun getAll(): List<Tag> {
        return transaction {
            TagsTable.selectAll().map {
                Tag(id = it[TagsTable.id], name = it[TagsTable.nombre])
            }
        }
    }

    override fun addTagToMovie(movieId: Int, tagId: Int) {
        transaction {
            MovieTagsTable.insert {
                it[peliculaId] = movieId
                it[etiquetaId] = tagId
            }
        }
    }

    override fun getTagsByMovie(movieId: Int): List<Tag> {
        return transaction {
            (MovieTagsTable innerJoin TagsTable)
                .select { MovieTagsTable.peliculaId eq movieId }
                .map {
                    Tag(id = it[TagsTable.id], name = it[TagsTable.nombre])
                }
        }
    }
}