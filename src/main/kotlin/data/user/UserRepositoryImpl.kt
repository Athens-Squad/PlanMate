package net.thechance.data.authentication

import logic.entities.User
import logic.repositories.UserRepository

class UserRepositoryImpl: UserRepository {
    override fun createUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUserByUsername(userName: String): User? {
        TODO("Not yet implemented")
    }
}