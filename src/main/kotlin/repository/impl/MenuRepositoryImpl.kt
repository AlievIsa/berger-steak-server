package com.alievisa.repository.impl

import com.alievisa.model.CategoryModel
import com.alievisa.model.DishModel
import com.alievisa.model.MenuModel
import com.alievisa.model.table.CategoryTable
import com.alievisa.model.table.DishTable
import com.alievisa.repository.api.MenuRepository
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class MenuRepositoryImpl : MenuRepository {

    override suspend fun getMenu(): MenuModel {
        return dbQuery {
            val categories = CategoryTable.selectAll().map { it to mutableListOf<DishModel>() }
            val categoryMap = categories.associateBy(
                keySelector = { it.first[CategoryTable.id] },
                valueTransform = { it.second }
            )

            DishTable.selectAll().forEach { row ->
                val dish = rowToDish(row)
                val categoryId = row[DishTable.categoryId]
                dish?.let {
                    categoryMap[categoryId]?.add(it)
                }
            }

            MenuModel(
                categories = categories.map { (row, dishes) ->
                    CategoryModel(
                        id = row[CategoryTable.id],
                        name = row[CategoryTable.name],
                        dishes = dishes,
                    )
                }
            )
        }
    }

    private fun rowToDish(row: ResultRow?): DishModel? {
        return row?.let {
            DishModel(
                id = it[DishTable.id],
                name = it[DishTable.name],
                price = it[DishTable.price],
                description = it[DishTable.description],
                image = it[DishTable.image],
            )
        }
    }
}