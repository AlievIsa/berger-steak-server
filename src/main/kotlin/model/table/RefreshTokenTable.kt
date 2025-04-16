package com.alievisa.model.table

import org.jetbrains.exposed.sql.Table

object RefreshTokenTable : Table("refresh_tokens") {
    val deviceId = varchar("device_id", 100)
    val userId = integer("user_id").references(UserTable.id)
    val token = varchar("token", 512)

    override val primaryKey = PrimaryKey(deviceId)
}