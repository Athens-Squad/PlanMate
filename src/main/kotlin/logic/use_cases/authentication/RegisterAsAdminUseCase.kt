package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(adminUser: User): Boolean {
        return false
    }
}