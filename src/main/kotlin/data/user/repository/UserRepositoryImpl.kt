package data.user.repository

import logic.entities.User
import logic.repositories.UserRepository
import data.user.data_source.UsersDataSource

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UserRepository {
    override fun createUser(user: User) {
        return usersDataSource.createUser(user)
    }

    override fun getUserByUsername(userName: String):User {
        return usersDataSource.getUserByUsername(userName)
    }

}