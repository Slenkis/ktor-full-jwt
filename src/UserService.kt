package com.github.slenkis

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val active: Boolean
)

object Users : Table("users") {
    val id = integer("id")
    override val primaryKey = PrimaryKey(id)
    val email = varchar("email", 50)
    val password = varchar("password", 80)
    val active = bool("active")
}

fun getAllUsers(): List<User> = transaction {
    Users.selectAll().map { toUser(it) }
}

fun getUserByEmail(email: String): User? = transaction {
    Users.select {
        (Users.email eq email)
    }.mapNotNull { toUser(it) }
        .singleOrNull()
}

private fun toUser(row: ResultRow): User =
    User(
        id = row[Users.id],
        email = row[Users.email],
        password = row[Users.password],
        active = row[Users.active]
    )