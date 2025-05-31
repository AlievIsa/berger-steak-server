package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object DishTable : Table("dishes") {
    val id = integer("id").autoIncrement()
    val categoryId = integer("category_id").references(CategoryTable.id)
    val name = varchar("name", 100)
    val price = integer("price")
    val description = varchar("description", 500).nullable()
    val image = varchar("image", 255).nullable()
    val weight = integer("weight").nullable()
    val calories = integer("calories").nullable()

    override val primaryKey = PrimaryKey(id)
}