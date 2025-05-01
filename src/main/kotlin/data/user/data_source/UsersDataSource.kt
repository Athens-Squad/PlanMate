package data.user.data_source

import logic.entities.User

interface UsersDataSource {
    fun createUser(user: User): Result<Unit>
    fun getUserByUsername(userName: String): Result<User>
    fun getAllUsers(): List<User>
}