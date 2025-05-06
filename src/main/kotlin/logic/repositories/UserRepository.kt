package logic.repositories

import logic.entities.User

interface UserRepository {
    fun createUser(user: User)
    fun getUserByUsername(userName: String): User
}
