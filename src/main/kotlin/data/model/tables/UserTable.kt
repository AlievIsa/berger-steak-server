package com.alievisa.data.model.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 100)
    val phoneNumber: Column<String> = varchar("phone_number", 20).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}