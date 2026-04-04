package org.example.users.infra.persistence

import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresUserRepository : UserRepository {

    override fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.select { UsersTable.correo eq email }
                .map { row ->
                    User(
                        id = row[UsersTable.id],
                        email = row[UsersTable.correo],
                        passwordHash = row[UsersTable.contrasena],
                        role = row[UsersTable.rol]
                    )
                }
                .singleOrNull()
        }
    }

    override fun save(user: User): User {
        return transaction {
            val insertStatement = UsersTable.insert {
                it[correo] = user.email
                it[contrasena] = user.passwordHash
                it[rol] = user.role
            }

            user.copy(id = insertStatement[UsersTable.id])
        }
    }
}