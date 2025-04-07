package com.alievisa.routes

import com.alievisa.data.model.dto.request.SendOtpRequest
import com.alievisa.data.model.dto.request.UpdateUserRequest
import com.alievisa.data.model.dto.request.VerifyOtpRequest
import com.alievisa.data.model.dto.response.TokenResponse
import com.alievisa.domain.interactor.AuthInteractor
import com.alievisa.domain.interactor.UserInteractor
import com.alievisa.domain.model.UserModel
import com.alievisa.utils.Constants
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.userRoute(authInteractor: AuthInteractor, userInteractor: UserInteractor) {

    post("api/v1/send-otp") {
        val request = call.receiveNullable<SendOtpRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest, Constants.ERROR.BAD_REQUEST)
            return@post
        }

        try {
            authInteractor.sendOtp(request.phoneNumber)
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
            val isValid = authInteractor.verifyOtp(request.phoneNumber, request.code)
            if (!isValid) {
                call.respond(HttpStatusCode.Unauthorized, Constants.ERROR.INVALID_OTP_CODE)
                return@post
            }

            var user = userInteractor.getUserByPhoneNumber(request.phoneNumber)
            if (user == null) {
                val newUser = UserModel(
                    id = 0,
                    name = "",
                    phoneNumber = request.phoneNumber,
                    address = "",
                )
                userInteractor.addUser(newUser)
                user = userInteractor.getUserByPhoneNumber(request.phoneNumber)!!
            }
            val token = authInteractor.generateToken(user)
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
            userInteractor.updateUserInfo(user.id, request.name, request.address)
            call.respond(HttpStatusCode.OK, Constants.SUCCESS.USER_INFO_UPDATED)
        }
    }
}