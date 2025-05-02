package logic.repositories

import logic.entities.User

interface AuthenticationRepository {
    fun login(username: String, password: String): Result<User>
}

