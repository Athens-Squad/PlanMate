package net.thechance.data.authentication.repository

import logic.entities.User
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.user.data_source.UsersFileDataSource
import net.thechance.logic.repositories.AuthenticationRepository
import net.thechance.logic.use_cases.authentication.exceptions.UserNotFoundException

class AuthRepositoryImpl(
   private val usersFileDataSource: UsersFileDataSource

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