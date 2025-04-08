package com.alievisa.plugins

import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.MenuRepository
import com.alievisa.repository.api.UserRepository
import com.alievisa.routes.menuRoute
import com.alievisa.routes.userRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    authRepository: AuthRepository,
    userRepository: UserRepository,
    menuRepository: MenuRepository,
) {
    routing {
        userRoute(authRepository, userRepository)
        menuRoute(menuRepository)
    }
}
