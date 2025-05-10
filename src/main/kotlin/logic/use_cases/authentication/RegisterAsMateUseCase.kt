package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType
import net.thechance.logic.exceptions.*
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {
    suspend fun execute(mateUser: User) {
        when {
            userValidator.isUsernameNotValid(mateUser.name) -> {
                throw InvalidUsernameException()
            }

            userValidator.isPasswordNotValid(mateUser.password) -> {
                throw InvalidPasswordException()
            }

            userValidator.isTypeNotAdmin(mateUser.type) -> {
                throw NotAnMateUserException()
            }

            userValidator.userNameExist(mateUser.name) -> {
                throw MateUsernameAlreadyExistsException()
            }
        }
        val hashedPassword = passwordHashing.hash(mateUser.password)
        val mateUserWithHashedPassword = mateUser.copy(password = hashedPassword)
        userRepository.createUser(mateUserWithHashedPassword)


    }


}


