@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import kotlin.uuid.ExperimentalUuidApi

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {

   suspend fun execute(adminUser: User, password: String) {
         if (
            userValidator. isUsernameNotValid(adminUser.name) ||
            userValidator. isPasswordNotValid(password) ||
            userValidator.isTypeNotAdmin(adminUser.type) ||
            userValidator.userNameExist(adminUser.name)
        ) {
            throw Exception("Cannot Register!")
        }

        val hashedPassword = passwordHashing.hash(password)

        userRepository.createUser(adminUser, hashedPassword)

    }


}