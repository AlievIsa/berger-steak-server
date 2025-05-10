package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val mail = varchar("mail", 30)
    val phoneNumber = varchar("phone_number", 20).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}