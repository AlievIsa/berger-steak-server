package com.alievisa.plugins

import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.UserRepository
import com.alievisa.routes.userRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(authRepository: AuthRepository, userRepository: UserRepository) {
    routing {
        userRoute(authRepository, userRepository)
    }
}
