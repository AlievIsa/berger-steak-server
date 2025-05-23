package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object RestaurantTable : Table("restaurants") {
    val id = integer("id").autoIncrement()
    val address = varchar("address", 255)
    val location = varchar("location", 255)

    override val primaryKey = PrimaryKey(id)
}