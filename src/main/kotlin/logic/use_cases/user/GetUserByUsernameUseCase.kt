package net.thechance.logic.use_cases.user

import logic.entities.User
import logic.repositories.UserRepository

class GetUserByUsernameUseCase(private val userRepository: UserRepository) {
    fun execute(userName: String): Result<User> {
        return userRepository.getUserByUsername(userName)
    }
}