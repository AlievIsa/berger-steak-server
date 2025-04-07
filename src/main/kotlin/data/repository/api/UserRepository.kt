package com.alievisa.data.repository.api

import com.alievisa.domain.model.UserModel

interface UserRepository {

    suspend fun getUserById(id: Int): UserModel?

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserModel?

    suspend fun addUser(userModel: UserModel)

    suspend fun updateUserInfo(id: Int, name: String, address: String)
}