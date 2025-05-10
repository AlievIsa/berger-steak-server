package com.alievisa.repository.impl

import com.alievisa.model.OrderModel
import com.alievisa.model.OrderStatus
import com.alievisa.model.OrderStatus.Companion.statusFromString
import com.alievisa.model.table.OrderTable
import com.alievisa.model.table.PositionTable
import com.alievisa.repository.api.OrderRepository
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class OrderRepositoryImpl : OrderRepository {

    override suspend fun createOrder(userId: Int, orderModel: OrderModel) {
        dbQuery {
            val orderId = OrderTable.insert {
                it[this.userId] = userId
                it[this.restaurantId] = orderModel.restaurantId
                it[this.price] = orderModel.price
                it[this.timestamp] = orderModel.timestamp
                it[this.status] = orderModel.status.value
            }[OrderTable.id]

            orderModel.positions.forEach { position ->
                PositionTable.insert {
                    it[this.orderId] = orderId
                    it[this.dishId] = position.dishId
                    it[this.dishAmount] = position.dishAmount
                }
            }
        }
    }

    override suspend fun getOrderStatusById(orderId: Int): OrderStatus? {
        return dbQuery {
            OrderTable.selectAll().where { OrderTable.id eq orderId }
                .mapNotNull { statusFromString(it[OrderTable.status]) }.singleOrNull()
        }
    }

    override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        dbQuery {
            OrderTable.update({ OrderTable.id eq orderId }) {
                it[this.status] = status.value
            }
        }
    }
}