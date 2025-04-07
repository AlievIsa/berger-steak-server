package com.alievisa.data.model.table

import org.jetbrains.exposed.sql.Table

object RestaurantTable : Table("restaurants") {
    val id = integer("id").autoIncrement()
    val address = varchar("address", 255)

    override val primaryKey = PrimaryKey(id)
}