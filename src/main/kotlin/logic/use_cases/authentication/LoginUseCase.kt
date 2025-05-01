package net.thechance.logic.use_cases.authentication

import logic.entities.User
import net.thechance.data.authentication.utils.PasswordHashing
import net.thechance.logic.repositories.AuthenticationRepository
import net.thechance.logic.use_cases.authentication.exceptions.InvalidCredentialsException

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val passwordHashing: PasswordHashing,
) {
    fun execute(username: String, password: String): Result<User> {
        return if (
            isUsernameNotValid(username) ||
            isPasswordNotValid(password)
        ) {
            Result.failure(InvalidCredentialsException())
        } else {
            runCatching {
                val hashedPassword = passwordHashing.hash(password)
                authenticationRepository.login(username, hashedPassword).getOrThrow()
            }
        }
    }


    private fun isUsernameNotValid(userName: String) = userName.isEmpty()
    private fun isPasswordNotValid(password: String) = password.isEmpty()
}
