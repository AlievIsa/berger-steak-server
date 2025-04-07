package com.alievisa.data.model.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val phoneNumber = varchar("phone_number", 20).uniqueIndex()
    val address = varchar("address", 255)

    override val primaryKey = PrimaryKey(id)
}