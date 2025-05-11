package data.user.data_source

import logic.entities.User
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto

interface UsersDataSource {
    suspend fun createUser(user: User, password: String)
    suspend fun getUserByUsername(userName: String): User
    suspend fun getAllUsers(): List<UserDto>
}