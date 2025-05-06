package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {
    suspend fun execute(mateUser: User) {
        if (
            userValidator.isUsernameNotValid(mateUser.name) ||
            userValidator.isPasswordNotValid(mateUser.password) ||
            userValidator.isTypeNotMate(mateUser.type) ||
            userValidator.isMateAdminIdNotValid(mateUser.type) ||
            userValidator.userNameExist(mateUser.name)
        ) {
            throw Exception("Cannot Register!")
        }
        val hashedPassword = passwordHashing.hash(mateUser.password)
        val mateUserWithHashedPassword = mateUser.copy(password = hashedPassword)
        userRepository.createUser(mateUserWithHashedPassword)


    }


}


