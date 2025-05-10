package com.alievisa.repository.api

import com.alievisa.model.UserModel

interface UserRepository {

    suspend fun getUserById(id: Int): UserModel?

    suspend fun getUserByMail(mail: String): UserModel?

    suspend fun addUser(userModel: UserModel)

    suspend fun deleteUserById(id: Int)

    suspend fun updateUserInfo(id: Int, name: String, phoneNumber: String, mail: String)
}