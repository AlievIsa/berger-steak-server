package com.alievisa.repository.impl

import com.alievisa.model.BasketModel
import com.alievisa.model.DishModel
import com.alievisa.model.table.CategoryTable
import com.alievisa.model.table.DishTable
import com.alievisa.repository.api.RecommendationsRepository
import com.alievisa.utils.dbQuery
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class RecommendationsRepositoryImpl: RecommendationsRepository {

    override suspend fun getRecommendations(basketModel: BasketModel): List<DishModel> {
        val selectedDishIds = basketModel.positions.map { it.dishId }.toSet()
        val selectedCategories = mutableSetOf<Int>()

        for (position in basketModel.positions) {
            val dish = getDishById(position.dishId)
            if (dish != null) {
                selectedCategories.add(dish.categoryId)
            }
        }

        val popularDishIds = listOf(32, 5, 23, 19, 30, 6, 25, 9)
        val recommendations = mutableListOf<DishModel>()

        val hour = LocalDateTime.now().hour

        // Завтраки (6-11 утра)
        if (hour in 6..11 && 1 !in selectedCategories) {
            val breakfasts = getDishesByCategory(1).filter { it.id !in selectedDishIds }
            val randomCount = (1..2).random()
            recommendations += breakfasts.shuffled().take(randomCount)
        }

        // Напитки
        if (8 !in selectedCategories) {
            val drinks = getDishesByCategory(8).filter { it.id !in selectedDishIds }
            val randomCount = (1..2).random()
            recommendations += drinks.shuffled().take(randomCount)
        }

        // Закуски
        if (5 !in selectedCategories) {
            val snacks = getDishesByCategory(5).filter { it.id !in selectedDishIds }
            val randomCount = (1..2).random()
            recommendations += snacks.shuffled().take(randomCount)
        }

        // Соусы (если закуски есть, а соусов нет — добавляем только 1)
        if (5 in selectedCategories && 9 !in selectedCategories) {
            val sauces = getDishesByCategory(9).filter { it.id !in selectedDishIds }
            sauces.shuffled().firstOrNull()?.let { recommendations.add(it) }
        }

        // Популярные блюда для закрытия остальных категорий
        val allCategories = getAllCategoryIds()
        val missingCategories = allCategories - selectedCategories

        for (categoryId in missingCategories) {
            val dishes = getDishesByCategory(categoryId).filter { it.id !in selectedDishIds }
            val popularDishes = dishes.filter { it.id in popularDishIds }
            val randomCount = (1..2).random()
            recommendations += popularDishes.shuffled().take(randomCount)
        }

        // Убираем дубликаты
        return recommendations.distinctBy { it.id }
    }

    private suspend fun getAllCategoryIds(): List<Int> = dbQuery {
        CategoryTable.selectAll().map { it[CategoryTable.id] }
    }

    private suspend fun getDishById(id: Int): DishModel? {
        return dbQuery {
            DishTable.selectAll().where { DishTable.id.eq(id) }.firstOrNull()?.rowToDish()
        }
    }

    private suspend fun getDishesByCategory(categoryId: Int): List<DishModel> {
        return dbQuery {
            DishTable.selectAll().where { DishTable.categoryId.eq(categoryId) }.mapNotNull { it.rowToDish() }
        }
    }

    private fun ResultRow?.rowToDish(): DishModel? {
        return this?.let {
            DishModel(
                id = it[DishTable.id],
                categoryId = it[DishTable.categoryId],
                name = it[DishTable.name],
                price = it[DishTable.price],
                description = it[DishTable.description],
                image = it[DishTable.image],
                weight = it[DishTable.weight],
                calories = it[DishTable.calories]
            )
        }
    }
}