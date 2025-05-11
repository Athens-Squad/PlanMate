package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.exceptions.AdminUsernameAlreadyExistsException
import net.thechance.logic.exceptions.InvalidPasswordException
import net.thechance.logic.exceptions.InvalidUsernameException
import net.thechance.logic.exceptions.NotAnAdminUserException
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
                throw NotAnAdminUserException()
            }

            userValidator.userNameExist(mateUser.name) -> {
                throw AdminUsernameAlreadyExistsException()
            }
        }
        val hashedPassword = passwordHashing.hash(mateUser.password)
        val mateUserWithHashedPassword = mateUser.copy(password = hashedPassword)
        userRepository.createUser(mateUserWithHashedPassword)


    }


}


