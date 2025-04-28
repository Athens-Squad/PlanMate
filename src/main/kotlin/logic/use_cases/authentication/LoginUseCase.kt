package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.AuditRepository
import logic.repositories.UserRepository
import net.thechance.logic.repositories.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val hashPasswordUseCase: HashPasswordUseCase,
) {
    fun execute(username: String, password: String): User? {
        val hashedPassword = hashPasswordUseCase.execute(password)

        return authenticationRepository.login(username,hashedPassword)

    }
}
