package logic.repositories

import logic.entities.User

interface UserRepository {
    fun createUser(user: User): Result<Unit>
    fun getUserByUsername(userName: String): Result<User>
}
