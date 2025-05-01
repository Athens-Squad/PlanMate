package data.user

import logic.entities.User
import logic.repositories.UserRepository

class UserRepositoryImpl: UserRepository {
    override fun createUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUserByUsername(userName: String): Result<User> {
        TODO("Not yet implemented")
    }
}