package com.alievisa.data.repository.impl

import com.alievisa.data.model.table.UserTable
import com.alievisa.data.repository.api.UserRepository
import com.alievisa.domain.model.UserModel
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl : UserRepository {

    override suspend fun getUserById(id: Int): UserModel? {
        return dbQuery {
            UserTable.selectAll().where { UserTable.id.eq(id) }
                .map { rowToUser(row = it) }
                .singleOrNull()
        }
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserModel? {
        return dbQuery {
            UserTable.selectAll().where { UserTable.phoneNumber.eq(phoneNumber) }
                .map { rowToUser(row = it) }
                .singleOrNull()
        }
    }

    override suspend fun addUser(userModel: UserModel) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[name] = userModel.name
                userTable[phoneNumber] = userModel.phoneNumber
                userTable[address] = userModel.address
            }
        }
    }

    override suspend fun updateUserInfo(id: Int, name: String, address: String) {
        dbQuery {
            UserTable.update(where = { UserTable.id.eq(id) }) { userTable ->
                userTable[this.name] = name
                userTable[this.address] = address
            }
        }
    }

    private fun rowToUser(row: ResultRow?): UserModel? {
        return row?.let {
            UserModel(
                id = it[UserTable.id],
                name = it[UserTable.name],
                phoneNumber = it[UserTable.phoneNumber],
                address = it[UserTable.address],
            )
        }
    }
}