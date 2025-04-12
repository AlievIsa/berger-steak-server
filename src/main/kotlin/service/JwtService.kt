package com.alievisa.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtService(
    private val accessTokenMinutesToLive: Long = 15,
    private val refreshTokenDaysToLive: Long = 30,
) {

    private val issuer = System.getenv("JWT_ISSUER") ?: throw IllegalStateException("JWT_ISSUER is not set")
    private val accessSecret =
        System.getenv("JWT_ACCESS_TOKEN_SECRET") ?: throw IllegalStateException("JWT_ACCESS_TOKEN_SECRET is not set")
    private val refreshSecret =
        System.getenv("JWT_REFRESH_TOKEN_SECRET") ?: throw IllegalStateException("JWT_REFRESH_TOKEN_SECRET is not set")

    private val accessAlgorithm = Algorithm.HMAC256(accessSecret)
    private val refreshAlgorithm = Algorithm.HMAC256(refreshSecret)

    fun generateAccessToken(userId: Int): String {
        val expirationTimeMillis = System.currentTimeMillis() + accessTokenMinutesToLive * 60 * 1000
        return JWT.create()
            .withSubject(userId.toString())
            .withIssuer(issuer)
            .withExpiresAt(Date(expirationTimeMillis))
            .sign(accessAlgorithm)
    }

    fun generateRefreshToken(userId: Int): String {
        val expirationTimeMillis = System.currentTimeMillis() + refreshTokenDaysToLive * 24 * 60 * 60 * 1000
        return JWT.create()
            .withSubject(userId.toString())
            .withIssuer(issuer)
            .withExpiresAt(Date(expirationTimeMillis))
            .sign(refreshAlgorithm)
    }

    fun getAccessVerifier(): JWTVerifier =
        JWT.require(accessAlgorithm).withIssuer(issuer).build()

    fun getRefreshVerifier(): JWTVerifier =
        JWT.require(refreshAlgorithm).withIssuer(issuer).build()
}