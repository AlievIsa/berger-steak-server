package com.alievisa.plugins

import com.alievisa.domain.interactor.AuthInteractor
import com.alievisa.domain.interactor.UserInteractor
import com.alievisa.routes.userRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(authInteractor: AuthInteractor, userInteractor: UserInteractor) {
    routing {
        userRoute(authInteractor, userInteractor)
    }
}
