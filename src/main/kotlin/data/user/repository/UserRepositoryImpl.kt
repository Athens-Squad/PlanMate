package net.thechance.data.user.repository

import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.data.user.data_source.UsersDataSource

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UserRepository {
    override fun createUser(user: User): Result<Unit> {
        return usersDataSource.createUser(user)
    }

    override fun getUserByUsername(userName: String): Result<User> {
        return usersDataSource.getUserByUsername(userName)
    }

}