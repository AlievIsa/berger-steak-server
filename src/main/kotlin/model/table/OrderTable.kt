package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object OrderTable : Table("orders") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)
    val restaurantId = integer("restaurant_id").references(RestaurantTable.id)
    val price = integer("price")
    val timestamp = timestamp("timestamp")
    val status = varchar("status", 50)

    override val primaryKey = PrimaryKey(id)
}