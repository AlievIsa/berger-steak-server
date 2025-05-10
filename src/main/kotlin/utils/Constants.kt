package com.alievisa.utils

class Constants {

    object SUCCESS {
        const val USER_INFO_UPDATED = "User info updated successfully"
        const val OTP_SEND_SUCCESSFULLY = "OTP sent successfully"
        const val LOGOUT_SUCCESSFUL = "Logout successful"
        const val ORDER_CREATED = "Order created successfully"
        const val ORDER_STATUS_UPDATED = "Order status updated successfully"
    }

    object ERROR {
        const val INVALID_OTP_CODE = "Invalid otp code"
        const val OTP_VERIFICATION_FAILED = "OTP verification failed"
        const val BAD_REQUEST = "Bad request"
        const val FAILED_TO_SEND_OTP = "Failed to send OTP"
        const val GENERAL = "Something went wrong"
        const val UNAUTHORIZED = "Unauthorized"
        const val INVALID_TOKEN_PAYLOAD = "Invalid token payload"
        const val INVALID_REFRESH_TOKEN = "Invalid refresh token"
        const val TOKEN_VERIFICATION_FAILED = "Token verification failed"
        const val INVALID_DEVICE_ID = "Invalid device id"
        const val REFRESH_TOKEN_WAS_STOLEN = "Refresh token was stolen"
        const val INVALID_ORDER_STATUS = "Invalid order status"
        const val INVALID_ORDER_ID = "Invalid order id"
    }
}