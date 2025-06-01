package com.alievisa.repository.api

import com.alievisa.model.BasketModel
import com.alievisa.model.DishModel

interface RecommendationsRepository {

    suspend fun getRecommendations(basketModel: BasketModel): List<DishModel>

}