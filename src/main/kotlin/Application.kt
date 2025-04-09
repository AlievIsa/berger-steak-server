package com.alievisa

import com.alievisa.plugins.configureDatabase
import com.alievisa.plugins.configureMonitoring
import com.alievisa.plugins.configureRouting
import com.alievisa.plugins.configureSecurity
import com.alievisa.plugins.configureSerialization
import com.alievisa.repository.impl.AuthRepositoryImpl
import com.alievisa.repository.impl.MenuRepositoryImpl
import com.alievisa.repository.impl.RestaurantRepositoryImpl
import com.alievisa.repository.impl.UserRepositoryImpl
import com.alievisa.service.ExolveSmsService
import com.alievisa.service.JwtService
import com.alievisa.service.OtpService
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val authRepository = AuthRepositoryImpl(
        jwtService = JwtService(),
        otpService = OtpService(),
        smsService = ExolveSmsService()
    )
    val userRepository = UserRepositoryImpl()
    val menuRepository = MenuRepositoryImpl()
    val restaurantRepository = RestaurantRepositoryImpl()

    configureDatabase()
    configureSerialization()
    configureMonitoring()
    configureSecurity(authRepository, userRepository)
    configureRouting(
        authRepository = authRepository,
        userRepository = userRepository,
        menuRepository = menuRepository,
        restaurantRepository = restaurantRepository
    )
}
