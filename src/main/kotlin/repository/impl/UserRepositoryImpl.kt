package com.alievisa.repository.impl

import com.alievisa.model.UserModel
import com.alievisa.model.table.UserTable
import com.alievisa.repository.api.UserRepository
import com.alievisa.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
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

    override suspend fun getUserByMail(mail: String): UserModel? {
        return dbQuery {
            UserTable.selectAll().where { UserTable.mail.eq(mail) }
                .map { rowToUser(row = it) }
                .singleOrNull()
        }
    }

    override suspend fun addUser(userModel: UserModel) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[name] = userModel.name
                userTable[mail] = userModel.mail
                userTable[phoneNumber] = userModel.phoneNumber
                userTable[address] = userModel.address
            }
        }
    }

    override suspend fun deleteUserById(id: Int) {
        dbQuery {
            UserTable.deleteWhere { UserTable.id.eq(id) }
        }
    }

    override suspend fun updateUserInfo(id: Int, name: String, phoneNumber: String, address: String) {
        dbQuery {
            UserTable.update(where = { UserTable.id.eq(id) }) { userTable ->
                userTable[this.name] = name
                userTable[this.phoneNumber] = phoneNumber
                userTable[this.address] = address
            }
        }
    }

    private fun rowToUser(row: ResultRow?): UserModel? {
        return row?.let {
            UserModel(
                id = it[UserTable.id],
                name = it[UserTable.name],
                mail = it[UserTable.mail],
                phoneNumber = it[UserTable.phoneNumber],
                address = it[UserTable.address],
            )
        }
    }
}