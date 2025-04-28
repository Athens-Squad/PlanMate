package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.AuditRepository
import logic.repositories.UserRepository

class RegisterAsMateUseCase(
    private val authRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(mateUser: User): Boolean {

        return false
    }
}

