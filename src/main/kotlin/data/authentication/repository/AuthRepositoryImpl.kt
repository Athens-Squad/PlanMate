package data.authentication.repository

import data.user.data_source.UsersDataSource
import logic.entities.User
import logic.repositories.AuthenticationRepository
import logic.use_cases.authentication.exceptions.UserNotFoundException

class AuthRepositoryImpl(
   private val usersFileDataSource: UsersDataSource

): AuthenticationRepository {

    override fun login(username: String, password: String): Result<User> = runCatching {
        val users = getAllUsers()
        users.find { user ->
            user.name == username && user.password == password
        } ?: throw UserNotFoundException()
    }

    private fun getAllUsers(): List<User> {
        return usersFileDataSource.getAllUsers()
    }
}