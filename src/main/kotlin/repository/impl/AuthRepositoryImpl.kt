package com.alievisa.repository.impl

import com.alievisa.model.table.RefreshTokenTable
import com.alievisa.repository.api.AuthRepository
import com.alievisa.service.DeviceService
import com.alievisa.service.JwtService
import com.alievisa.service.MailService
import com.alievisa.service.OtpService
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class AuthRepositoryImpl(
    private val jwtService: JwtService,
    private val otpService: OtpService,
    private val mailService: MailService,
    private val deviceService: DeviceService,
) : AuthRepository {

    override suspend fun sendOtp(mail: String) {
        val code = generateCode()

        otpService.saveOtp(mail, code)
        mailService.sendMessage(mail, code)
    }

    override suspend fun verifyOtp(mail: String, code: String): Boolean {
        return otpService.verifyOtp(mail, code)
    }

    override fun generateAccessToken(userId: Int) = jwtService.generateAccessToken(userId)

    override fun generateRefreshToken(deviceId: String) = jwtService.generateRefreshToken(deviceId)

    override fun getAccessVerifier() = jwtService.getAccessVerifier()

    override fun getRefreshVerifier() = jwtService.getRefreshVerifier()

    override suspend fun getUserIdByDeviceId(deviceId: String): Int? {
        return dbQuery {
            RefreshTokenTable.selectAll().where { RefreshTokenTable.deviceId.eq(deviceId) }
                .map { it[RefreshTokenTable.userId] }
                .singleOrNull()
        }
    }

    override suspend fun saveRefreshToken(deviceId: String, userId: Int, token: String) {
        dbQuery {
            if (RefreshTokenTable.selectAll().where { RefreshTokenTable.deviceId.eq(deviceId) }.empty().not() ) {
                RefreshTokenTable.update(where = { RefreshTokenTable.deviceId.eq(deviceId) } ) {
                    it[this.token] = token
                }
            } else {
                RefreshTokenTable.insert {
                    it[this.deviceId] = deviceId
                    it[this.userId] = userId
                    it[this.token] = token
                }
            }
        }
    }

    override suspend fun checkIsDeviceIdSuspiciouslyUsed(deviceId: String): Boolean {
        if (deviceService.isDeviceIdSuspiciouslyUsed(deviceId)) {
            dbQuery {
                RefreshTokenTable.deleteWhere { RefreshTokenTable.deviceId.eq(deviceId) }
            }
            return true
        }
        return false
    }

    override suspend fun getRefreshTokenByDeviceId(deviceId: String): String? {
        return dbQuery {
            RefreshTokenTable.selectAll().where { RefreshTokenTable.deviceId.eq(deviceId) }
                .map { it[RefreshTokenTable.token] }
                .singleOrNull()
        }
    }

    override suspend fun deleteRefreshTokenByDeviceId(deviceId: String) {
        deviceService.releaseDeviceId(deviceId)
        dbQuery {
            RefreshTokenTable.deleteWhere { RefreshTokenTable.deviceId.eq(deviceId) }
        }
    }

    private fun generateCode(): String {
        return (1000..9999).random().toString()
    }
}