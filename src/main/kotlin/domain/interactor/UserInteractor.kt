package com.alievisa.domain.interactor

import com.alievisa.data.repository.api.UserRepository
import com.alievisa.domain.model.UserModel

class UserInteractor(
    private val repository: UserRepository,
) {

    suspend fun getUserById(id: Int) = repository.getUserById(id)

    suspend fun getUserByPhoneNumber(phoneNumber: String) = repository.getUserByPhoneNumber(phoneNumber)

    suspend fun addUser(userModel: UserModel) = repository.addUser(userModel)

    suspend fun updateUserInfo(id: Int, name: String, address: String) = repository.updateUserInfo(id, name, address)
}