package com.alievisa.routes

import com.alievisa.model.UserModel
import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.UserRepository
import com.alievisa.routes.request.LoginRequest
import com.alievisa.routes.request.LogoutRequest
import com.alievisa.routes.request.RefreshTokenRequest
import com.alievisa.routes.request.UpdateUserRequest
import com.alievisa.routes.request.VerifyOtpRequest
import com.alievisa.routes.response.AccessTokenResponse
import com.alievisa.routes.response.AuthTokensResponse
import com.alievisa.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.userRoute(authRepository: AuthRepository, userRepository: UserRepository) {

    post("api/v1/login") {
        val request = call.receiveNullable<LoginRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            authRepository.sendOtp(request.mail)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.OTP_SEND_SUCCESSFULLY)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "${Constants.ERROR.FAILED_TO_SEND_OTP}: ${e.message}")
        }
    }

    post("api/v1/verify-otp") {
        val request = call.receiveNullable<VerifyOtpRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            val isValid = authRepository.verifyOtp(request.mail, request.code)
            if (!isValid) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_OTP_CODE)
                return@post
            }

            var user = userRepository.getUserByMail(request.mail)
            if (user == null) {
                val newUser = UserModel(
                    id = 0,
                    name = "",
                    mail = request.mail,
                    phoneNumber = "",
                    address = "",
                )
                userRepository.addUser(newUser)
                user = userRepository.getUserByMail(request.mail)!!
            }
            val accessToken = authRepository.generateAccessToken(user.id)
            val refreshToken = authRepository.generateRefreshToken(request.deviceId)

            authRepository.saveRefreshToken(request.deviceId, user.id, refreshToken)

            val savedToken = authRepository.getRefreshTokenByDeviceId(request.deviceId)
            if (savedToken != refreshToken) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_REFRESH_TOKEN)
                return@post
            }

            call.respond(HttpStatusCode.OK, AuthTokensResponse(accessToken, refreshToken))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "${Constants.ERROR.OTP_VERIFICATION_FAILED}: ${e.message}")
        }
    }

    post("api/v1/refresh-token") {
        val request = call.receiveNullable<RefreshTokenRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            val decodedJWT = authRepository.getRefreshVerifier().verify(request.refreshToken)
            val deviceId = decodedJWT.subject

            if (deviceId == null) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_TOKEN_PAYLOAD)
                return@post
            }

            val isDeviceIdSuspiciouslyUsed = authRepository.checkIsDeviceIdSuspiciouslyUsed(deviceId)
            if (isDeviceIdSuspiciouslyUsed) {
                call.respond(HttpStatusCode.Conflict, Constants.ERROR.REFRESH_TOKEN_WAS_STOLEN)
                return@post
            }

            val savedToken = authRepository.getRefreshTokenByDeviceId(deviceId)
            if (savedToken != request.refreshToken) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_REFRESH_TOKEN)
                return@post
            }

            val userId = authRepository.getUserIdByDeviceId(deviceId)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_DEVICE_ID)
                return@post
            }

            val accessToken = authRepository.generateAccessToken(userId)
            call.respond(HttpStatusCode.OK, AccessTokenResponse(accessToken))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Unauthorized, "${Constants.ERROR.TOKEN_VERIFICATION_FAILED}: ${e.message}")
        }
    }

    post("api/v1/logout") {
        val request = call.receiveNullable<LogoutRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            val decodedJWT = authRepository.getRefreshVerifier().verify(request.refreshToken)
            val deviceId = decodedJWT.subject

            if (deviceId == null) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_TOKEN_PAYLOAD)
                return@post
            }

            authRepository.deleteRefreshTokenByDeviceId(deviceId)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.LOGOUT_SUCCESSFUL)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Unauthorized, "${Constants.ERROR.TOKEN_VERIFICATION_FAILED}: ${e.message}")
        }
    }

    authenticate("jwt") {
        get("api/v1/get-user-info") {
            val user = call.principal<UserModel>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.UNAUTHORIZED)
                return@get
            }
            call.respond(HttpStatusCode.OK, user)
        }

        post("api/v1/update-user-info") {
            val user = call.principal<UserModel>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.UNAUTHORIZED)
                return@post
            }
            val request = call.receiveNullable<UpdateUserRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
                return@post
            }
            userRepository.updateUserInfo(user.id, request.name, request.phoneNumber, request.address)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.USER_INFO_UPDATED)
        }
    }
}