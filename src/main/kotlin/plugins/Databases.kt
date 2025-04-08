package com.alievisa.plugins

import com.alievisa.model.table.CategoryTable
import com.alievisa.model.table.DishTable
import com.alievisa.model.table.OrderTable
import com.alievisa.model.table.PositionTable
import com.alievisa.model.table.RefreshTokenTable
import com.alievisa.model.table.RestaurantTable
import com.alievisa.model.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
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
            UserTable, OrderTable, DishTable, PositionTable, CategoryTable, RestaurantTable, RefreshTokenTable
        )
    }
}

class DatabaseConfig(private val dbUrl: String, private val dbUser: String, private val dbPassword: String) {
    fun createHikariDatasource(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            maximumPoolSize = System.getenv("DB_MAX_POOL_SIZE")?.toInt() ?: 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }
}
