package logic.repositories

import logic.entities.User

interface AuthenticationRepository {
   suspend fun login(username: String, password: String): User
}

