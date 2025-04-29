package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(mateUser: User): Boolean {

        return false
    }
}

