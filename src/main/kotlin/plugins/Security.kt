package com.alievisa.plugins

import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(authRepository: AuthRepository, userRepository: UserRepository) {

    authentication {
        jwt("jwt") {
            verifier(authRepository.getJwtVerifier())
            realm = "Service server"
            validate {
                val id = it.payload.getClaim("id").asInt()
                userRepository.getUserById(id = id)
            }
        }
    }
}
