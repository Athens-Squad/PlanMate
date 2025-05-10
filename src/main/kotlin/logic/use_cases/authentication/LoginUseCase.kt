package logic.use_cases.authentication

import logic.entities.User
import data.authentication.utils.PasswordHashing
import logic.repositories.AuthenticationRepository
import net.thechance.logic.exceptions.InvalidCredentialsException
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator,

) {
   suspend fun execute(username: String, password: String): User{
       if (
          userValidator. isUsernameNotValid(username) ||
            userValidator.isPasswordNotValid(password)
        ) {
           throw InvalidCredentialsException()
        }

        val hashedPassword = passwordHashing.hash(password)
        return authenticationRepository.login(username, hashedPassword)
    }


}
