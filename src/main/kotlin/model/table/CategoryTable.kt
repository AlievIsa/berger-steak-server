package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object CategoryTable : Table("categories") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)
}