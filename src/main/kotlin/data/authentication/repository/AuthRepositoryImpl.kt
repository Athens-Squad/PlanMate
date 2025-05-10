package data.authentication.repository

import data.user.data_source.UsersDataSource
import data.user.data_source.remote.mongo.mapper.toUser
import logic.entities.User
import logic.repositories.AuthenticationRepository
import logic.exceptions.UserNotFoundException
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto

class AuthRepositoryImpl(
    private val usersFileDataSource: UsersDataSource

) : AuthenticationRepository {

    override suspend fun login(username: String, password: String): User {
        val users = getAllUsers()

        return users.find { userDto ->

            userDto.name == username && userDto.password == password

        }?.toUser() ?: throw UserNotFoundException()
    }

    private suspend fun getAllUsers(): List<UserDto> {
        return usersFileDataSource.getAllUsers()
    }
}

// need to use UserDto instead