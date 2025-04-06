package com.alievisa.plugins

import com.alievisa.data.DatabaseConfig
import com.alievisa.data.model.tables.CategoryTable
import com.alievisa.data.model.tables.DishTable
import com.alievisa.data.model.tables.OrderTable
import com.alievisa.data.model.tables.PositionTable
import com.alievisa.data.model.tables.RestaurantTable
import com.alievisa.data.model.tables.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

private val dbUrl = System.getenv("DB_URL") ?: throw IllegalStateException("DB_URL is not set")
private val dbUser = System.getenv("DB_USER") ?: throw IllegalStateException("DB_USER is not set")
private val dbPassword = System.getenv("DB_PASSWORD") ?: throw IllegalStateException("DB_PASSWORD is not set")

fun Application.configureDatabase() {
    val dbConfig = DatabaseConfig(dbUrl, dbUser, dbPassword)
    Database.connect(dbConfig.createHikariDatasource())

    transaction {
        SchemaUtils.create(
            UserTable, OrderTable, DishTable, PositionTable, CategoryTable, RestaurantTable
        )
    }
}
