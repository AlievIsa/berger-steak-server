package com.alievisa.data.model.table

import org.jetbrains.exposed.sql.Table

object DishTable : Table("dishes") {
    val id = integer("id").autoIncrement()
    val categoryId = integer("category_id").references(CategoryTable.id)
    val name = varchar("name", 100)
    val price = decimal("price", precision = 10, scale = 2)
    val description = varchar("description", 500)
    val image = varchar("image", 255)

    override val primaryKey = PrimaryKey(id)
}