package com.alievisa.repository.impl

import com.alievisa.model.OrderModel
import com.alievisa.model.PositionModel
import com.alievisa.model.UserModel
import com.alievisa.model.table.OrderTable
import com.alievisa.model.table.PositionTable
import com.alievisa.model.table.UserTable
import com.alievisa.repository.api.UserRepository
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl : UserRepository {

    override suspend fun getUserById(id: Int): UserModel? {
        val row = dbQuery {
            UserTable.selectAll().where { UserTable.id.eq(id) }.singleOrNull()
        }
        return rowToUser(row)
    }

    override suspend fun getUserByMail(mail: String): UserModel? {
        val row = dbQuery {
            UserTable.selectAll().where { UserTable.mail.eq(mail) }.singleOrNull()
        }
        return rowToUser(row)
    }

    override suspend fun addUser(userModel: UserModel) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[name] = userModel.name
                userTable[mail] = userModel.mail
                userTable[phoneNumber] = userModel.phoneNumber
                userTable[mail] = userModel.mail
            }
        }
    }

    override suspend fun deleteUserById(id: Int) {
        dbQuery {
            UserTable.deleteWhere { UserTable.id.eq(id) }
        }
    }

    override suspend fun updateUserInfo(id: Int, name: String, phoneNumber: String, mail: String) {
        dbQuery {
            UserTable.update(where = { UserTable.id.eq(id) }) { userTable ->
                userTable[this.name] = name
                userTable[this.phoneNumber] = phoneNumber
                userTable[this.mail] = mail
            }
        }
    }

    private suspend fun rowToUser(userRow: ResultRow?): UserModel? {
        return userRow?.let {
            val userId = it[UserTable.id]
            val ordersRow = getOrderRowsByUserId(userId)
            val orders = ordersRow.map { orderRow ->
                val orderId = orderRow[OrderTable.id]
                val positions = getPositionsByOrderId(orderId)
                OrderModel(
                    id = orderId,
                    restaurantId = orderRow[OrderTable.restaurantId],
                    price = orderRow[OrderTable.price],
                    timestamp = orderRow[OrderTable.timestamp],
                    status = orderRow[OrderTable.status],
                    positions = positions,
                )
            }

            UserModel(
                id = userId,
                name = it[UserTable.name],
                mail = it[UserTable.mail],
                phoneNumber = it[UserTable.phoneNumber],
                orders = orders,
            )
        }
    }

    private suspend fun getOrderRowsByUserId(userId: Int): List<ResultRow> = dbQuery {
        OrderTable.selectAll().where { OrderTable.userId eq userId }.toList()
    }

    private suspend fun getPositionsByOrderId(orderId: Int): List<PositionModel> = dbQuery {
        PositionTable.selectAll().where { PositionTable.orderId eq orderId }.map { row ->
            PositionModel(
                id = row[PositionTable.id],
                dishId = row[PositionTable.dishId],
                dishAmount = row[PositionTable.dishAmount],
            )
        }
    }
}