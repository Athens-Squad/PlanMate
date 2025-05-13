@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.validators.uservalidation.UserValidator
import kotlin.uuid.ExperimentalUuidApi

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {
    suspend fun execute(mateUser: User, password: String) {

            userValidator.isUsernameNotValid(mateUser.name)
            userValidator.isPasswordNotValid(password)
            userValidator.isTypeNotMate(mateUser.type)
            userValidator.isMateAdminIdNotValid(mateUser.type)
            userValidator.userNameExist(mateUser.name)

        val hashedPassword = passwordHashing.hash(password)
        userRepository.createUser(mateUser, hashedPassword)


    }


}


