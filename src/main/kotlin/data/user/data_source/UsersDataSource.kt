package data.user.data_source

import logic.entities.User

interface UsersDataSource {
   suspend fun createUser(user: User): Unit
   suspend fun getUserByUsername(userName: String): User
    suspend fun getAllUsers(): List<User>
}