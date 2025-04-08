package com.alievisa.repository.api

import com.alievisa.model.MenuModel

interface MenuRepository {

    suspend fun getMenu(): MenuModel
}