package com.alievisa.repository.api

import com.alievisa.model.RestaurantModel

interface RestaurantRepository {

    suspend fun getRestaurants(): List<RestaurantModel>
}