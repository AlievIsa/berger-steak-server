package com.alievisa.repository.api

import com.alievisa.model.OrderModel
import com.alievisa.model.OrderStatus

interface OrderRepository {

    suspend fun createOrder(userId: Int, orderModel: OrderModel)

    suspend fun getOrderStatusById(orderId: Int): OrderStatus?

    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus)
}