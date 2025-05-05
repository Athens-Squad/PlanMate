package data.user.data_source

import logic.entities.User

interface UsersDataSource {
    fun createUser(user: User): Unit
    fun getUserByUsername(userName: String): User
    fun getAllUsers(): List<User>
}