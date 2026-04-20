package org.example.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.example.custom_lists.infra.persistence.CustomListsTable
import org.example.favorites.infra.persistence.FavoritesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = dotenv["DB_JDBC_URL"]
                ?: throw IllegalArgumentException("Falta DB_JDBC_URL en el .env")
            driverClassName = "org.postgresql.Driver"
            username = dotenv["DB_USER"]
                ?: throw IllegalArgumentException("Falta DB_USER en el .env")
            password = dotenv["DB_PASSWORD"]
                ?: throw IllegalArgumentException("Falta DB_PASSWORD en el .env")

            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(CustomListsTable, FavoritesTable)
        }
        println("Conexión a AWS RDS establecida exitosamente de forma segura.")
    }

    fun getEnv(key: String): String {
        return dotenv[key] ?: throw IllegalArgumentException("Falta la variable $key en el .env")
    }
}
