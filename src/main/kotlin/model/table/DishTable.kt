package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object DishTable : Table("dishes") {
    val id = integer("id").autoIncrement()
    val categoryId = integer("category_id").references(CategoryTable.id)
    val name = varchar("name", 100)
    val price = integer("price")
    val description = varchar("description", 500)
    val image = varchar("image", 255)
    val weight = integer("weight")
    val calories = integer("calories")

    override val primaryKey = PrimaryKey(id)
}