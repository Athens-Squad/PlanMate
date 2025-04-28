package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.AuditRepository
import logic.repositories.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(username: String, password: String): User? {
        return null
    }
}
