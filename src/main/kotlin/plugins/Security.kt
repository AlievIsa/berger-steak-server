package com.alievisa.plugins

import com.alievisa.domain.interactor.AuthInteractor
import com.alievisa.domain.interactor.UserInteractor
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(authInteractor: AuthInteractor, userInteractor: UserInteractor) {

    authentication {
        jwt("jwt") {
            verifier(authInteractor.getJwtVerifier())
            realm = "Service server"
            validate {
                val id = it.payload.getClaim("id").asInt()
                userInteractor.getUserById(id = id)
            }
        }
    }
}
