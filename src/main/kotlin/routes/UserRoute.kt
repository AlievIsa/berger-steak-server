package com.alievisa.routes

import com.alievisa.model.UserModel
import com.alievisa.repository.api.AuthRepository
import com.alievisa.repository.api.UserRepository
import com.alievisa.routes.request.SendOtpRequest
import com.alievisa.routes.request.UpdateUserRequest
import com.alievisa.routes.request.VerifyOtpRequest
import com.alievisa.routes.response.TokenResponse
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

    post("api/v1/send-otp") {
        val request = call.receiveNullable<SendOtpRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            authRepository.sendOtp(request.phoneNumber)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.OTP_SEND_SUCCESSFULLY)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "${Constants.ERROR.FAILED_TO_SEND_OTP}: ${e.message}")
        }
    }

    post("api/v1/verify-otp") {
        val request = call.receiveNullable<VerifyOtpRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            val isValid = authRepository.verifyOtp(request.phoneNumber, request.code)
            if (!isValid) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_OTP_CODE)
                return@post
            }

            var user = userRepository.getUserByPhoneNumber(request.phoneNumber)
            if (user == null) {
                val newUser = UserModel(
                    id = 0,
                    name = "",
                    phoneNumber = request.phoneNumber,
                    address = "",
                )
                userRepository.addUser(newUser)
                user = userRepository.getUserByPhoneNumber(request.phoneNumber)!!
            }
            val token = authRepository.generateToken(user)
            call.respond(HttpStatusCode.OK, TokenResponse(token))

        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "${Constants.ERROR.OTP_VERIFICATION_FAILED}: ${e.message}")
        }
    }

    authenticate("jwt") {
        get("api/v1/get-user-info") {
            val user = call.principal<UserModel>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.UNAUTHORIZED)
                return@get
            }
            call.respond(user)
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
            userRepository.updateUserInfo(user.id, request.name, request.address)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.USER_INFO_UPDATED)
        }
    }
}