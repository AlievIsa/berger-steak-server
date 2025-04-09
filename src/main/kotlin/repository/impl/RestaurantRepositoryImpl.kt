package com.alievisa.repository.impl

import com.alievisa.model.RestaurantModel
import com.alievisa.model.table.RestaurantTable
import com.alievisa.repository.api.RestaurantRepository
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class RestaurantRepositoryImpl : RestaurantRepository {
    
    override suspend fun getRestaurants(): List<RestaurantModel> {
        return dbQuery { 
            RestaurantTable.selectAll().mapNotNull { rowToRestaurant(row = it) }
        }
    }

    private fun rowToRestaurant(row: ResultRow?): RestaurantModel? {
        return row?.let { 
            RestaurantModel(
                id = it[RestaurantTable.id],
                address = it[RestaurantTable.address],
                location = it[RestaurantTable.location],
            )
        }
    }
}