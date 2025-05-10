package data.user.repository

import logic.entities.User
import logic.repositories.UserRepository
import data.user.data_source.UsersDataSource

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UserRepository {
    override suspend fun createUser(user: User, password: String) {
        return usersDataSource.createUser(user, password)
    }

    override suspend fun getUserByUsername(userName: String):User {
        return usersDataSource.getUserByUsername(userName)
    }

}