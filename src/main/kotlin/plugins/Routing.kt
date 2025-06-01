package com.alievisa.plugins

import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.MenuRepository
import com.alievisa.repository.api.OrderRepository
import com.alievisa.repository.api.RecommendationsRepository
import com.alievisa.repository.api.RestaurantRepository
import com.alievisa.repository.api.UserRepository
import com.alievisa.routes.menuRoute
import com.alievisa.routes.orderRoute
import com.alievisa.routes.recommendationsRoute
import com.alievisa.routes.restaurantRoute
import com.alievisa.routes.userRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    authRepository: AuthRepository,
    userRepository: UserRepository,
    menuRepository: MenuRepository,
    restaurantRepository: RestaurantRepository,
    orderRepository: OrderRepository,
    recommendationsRepository: RecommendationsRepository,
) {
    routing {
        userRoute(authRepository, userRepository)
        menuRoute(menuRepository)
        restaurantRoute(restaurantRepository)
        orderRoute(orderRepository)
        recommendationsRoute(recommendationsRepository)
    }
}
