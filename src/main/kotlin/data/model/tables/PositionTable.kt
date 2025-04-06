package com.alievisa.data.model.tables

import org.jetbrains.exposed.sql.Table

object PositionTable : Table("positions") {
    val id = integer("id").autoIncrement()
    val orderId = integer("order_id").references(OrderTable.id)
    val dishId = integer("dish_id").references(DishTable.id)
    val dishAmount = integer("dish_amount")

    override val primaryKey = PrimaryKey(id)
}