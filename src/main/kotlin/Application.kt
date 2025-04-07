package com.alievisa

import com.alievisa.authentication.JwtService
import com.alievisa.data.repository.impl.AuthRepositoryImpl
import com.alievisa.data.repository.impl.UserRepositoryImpl
import com.alievisa.data.service.ExolveSmsSender
import com.alievisa.data.service.OtpService
import com.alievisa.domain.interactor.AuthInteractor
import com.alievisa.domain.interactor.UserInteractor
import com.alievisa.plugins.configureDatabase
import com.alievisa.plugins.configureMonitoring
import com.alievisa.plugins.configureRouting
import com.alievisa.plugins.configureSecurity
import com.alievisa.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val authInteractor = AuthInteractor(
        jwtService = JwtService(),
        repository = AuthRepositoryImpl(
            otpService = OtpService(),
            smsSender = ExolveSmsSender()
        )
    )
    val userInteractor = UserInteractor(repository = UserRepositoryImpl())

    configureDatabase()
    configureSerialization()
    configureMonitoring()
    configureSecurity(authInteractor, userInteractor)
    configureRouting(authInteractor, userInteractor)
}
