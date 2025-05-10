package logic.repositories

import logic.entities.User

interface UserRepository {
   suspend fun createUser(user: User, password: String)
   suspend fun getUserByUsername(userName: String): User
}
