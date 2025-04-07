package com.alievisa.repository.api

import com.alievisa.model.UserModel

interface UserRepository {

    suspend fun getUserById(id: Int): UserModel?

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserModel?

    suspend fun addUser(userModel: UserModel)

    suspend fun updateUserInfo(id: Int, name: String, address: String)
}