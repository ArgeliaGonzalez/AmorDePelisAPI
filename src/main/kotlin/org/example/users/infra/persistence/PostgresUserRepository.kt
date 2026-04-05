package org.example.users.infra.persistence

import org.example.users.domain.models.User
import org.example.users.domain.repository.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresUserRepository : UserRepository {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[UsersTable.id],
        email = row[UsersTable.correo],
        passwordHash = row[UsersTable.contrasena],
        role = row[UsersTable.rol],
        username = row[UsersTable.username],
        profileImageUrl = row[UsersTable.fotoPerfil]
    )

    override fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.select { UsersTable.correo eq email }.map(::resultRowToUser).singleOrNull()
        }
    }

    override fun findById(id: Int): User? {
        return transaction {
            UsersTable.select { UsersTable.id eq id }.map(::resultRowToUser).singleOrNull()
        }
    }

    override fun save(user: User): User {
        return transaction {
            val insertStatement = UsersTable.insert {
                it[correo] = user.email
                it[contrasena] = user.passwordHash
                it[rol] = user.role
                it[username] = user.username
                it[fotoPerfil] = user.profileImageUrl
            }
            user.copy(id = insertStatement[UsersTable.id])
        }
    }

    override fun update(user: User): User {
        transaction {
            UsersTable.update({ UsersTable.id eq user.id }) {
                it[correo] = user.email
                it[contrasena] = user.passwordHash
                it[rol] = user.role
                it[username] = user.username
                it[fotoPerfil] = user.profileImageUrl
            }
        }
        return user
    }

    override fun delete(id: Int) {
        transaction {
            UsersTable.deleteWhere { UsersTable.id eq id }
        }
    }
}