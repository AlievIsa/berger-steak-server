package com.alievisa.routes

import com.alievisa.model.OrderModel
import com.alievisa.model.OrderStatus
import com.alievisa.model.UserModel
import com.alievisa.repository.api.OrderRepository
import com.alievisa.routes.request.CreateOrderRequest
import com.alievisa.routes.request.UpdateOrderStatusRequest
import com.alievisa.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.orderRoute(orderRepository: OrderRepository) {

    authenticate("jwt") {
        post("v1/create-order") {
            val user = call.principal<UserModel>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.UNAUTHORIZED)
                return@post
            }

            val request = call.receiveNullable<CreateOrderRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
                return@post
            }

            val order = OrderModel(
                restaurantId = request.restaurantId,
                price = request.price,
                timestamp = request.timestamp,
                status = OrderStatus.CREATED,
                positions = request.positions,
            )

            orderRepository.createOrder(user.id, order)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.ORDER_CREATED)
        }
    }

    post("v1/update-order-status") {
        val request = call.receiveNullable<UpdateOrderStatusRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        val prevStatus = orderRepository.getOrderStatusById(request.orderId)
        if (prevStatus == null) {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.INVALID_ORDER_ID)
            return@post
        }

        val status = OrderStatus.statusFromString(request.status)

        if (status == null) {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.INVALID_ORDER_STATUS)
            return@post
        }

        orderRepository.updateOrderStatus(request.orderId, status)
        call.respond(HttpStatusCode.OK, "${Constants.SUCCESS.ORDER_STATUS_UPDATED} from ${prevStatus.value} to ${status.value}")
    }
}