package com.alievisa.plugins

import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(authRepository: AuthRepository, userRepository: UserRepository) {

    authentication {
        jwt("jwt") {
            verifier(authRepository.getAccessVerifier())
            realm = "Berger Steak"
            validate {
                val userId = it.payload.subject.toIntOrNull()
                userId?.let { id ->
                    userRepository.getUserById(id = id)
                }
            }
        }
    }
}
