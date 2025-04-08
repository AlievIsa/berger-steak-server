package com.alievisa.repository.impl

import com.alievisa.model.table.RefreshTokenTable
import com.alievisa.repository.api.AuthRepository
import com.alievisa.service.JwtService
import com.alievisa.service.OtpService
import com.alievisa.service.SmsService
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class AuthRepositoryImpl(
    private val jwtService: JwtService,
    private val otpService: OtpService,
    private val smsService: SmsService,
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String) {
        val code = generateCode()
        println("Generated OTP code for $phoneNumber: $code")

        otpService.saveOtp(phoneNumber, code)
        smsService.sendSms(phoneNumber, code)
    }

    override suspend fun verifyOtp(phoneNumber: String, code: String): Boolean {
        return otpService.verifyOtp(phoneNumber, code)
    }

    override fun generateAccessToken(userId: Int) = jwtService.generateAccessToken(userId)

    override fun generateRefreshToken(userId: Int) = jwtService.generateRefreshToken(userId)

    override fun getAccessVerifier() = jwtService.getAccessVerifier()

    override fun getRefreshVerifier() = jwtService.getRefreshVerifier()

    override suspend fun saveRefreshToken(userId: Int, token: String) {
        dbQuery {
            if (RefreshTokenTable.selectAll().where { RefreshTokenTable.userId.eq(userId) }.empty().not() ) {
                RefreshTokenTable.update(where = { RefreshTokenTable.userId.eq(userId) } ) {
                    it[this.token] = token
                }
            } else {
                RefreshTokenTable.insert {
                    it[this.userId] = userId
                    it[this.token] = token
                }
            }
        }
    }

    override suspend fun getRefreshTokenByUserId(userId: Int): String? {
        return dbQuery {
            RefreshTokenTable.selectAll().where { RefreshTokenTable.userId.eq(userId) }
                .map { it[RefreshTokenTable.token] }
                .singleOrNull()
        }
    }

    override suspend fun deleteRefreshTokenByUserId(userId: Int) {
        dbQuery {
            RefreshTokenTable.deleteWhere { RefreshTokenTable.userId.eq(userId) }
        }
    }

    private fun generateCode(): String {
        return (1000..9999).random().toString()
    }
}