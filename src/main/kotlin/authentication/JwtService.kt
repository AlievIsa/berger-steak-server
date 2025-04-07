package com.alievisa.authentication

import com.alievisa.domain.model.UserModel
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.time.LocalDateTime
import java.time.ZoneOffset

class JwtService {

    private val issuer = System.getenv("JWT_ISSUER") ?: throw IllegalStateException("JWT_ISSUER is not set")
    private val jwtSecret = System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET is not set")
    private val algorithm = Algorithm.HMAC256(jwtSecret)

    private val verifier: JWTVerifier = JWT
        .require(algorithm).withIssuer(issuer)
        .build()

    fun generateToken(user: UserModel): String {
        return JWT.create()
            .withSubject("AppAuthentication").withIssuer(issuer)
            .withClaim("id", user.id).withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))
            .sign(algorithm)
    }

    fun getVerifier(): JWTVerifier = verifier
}