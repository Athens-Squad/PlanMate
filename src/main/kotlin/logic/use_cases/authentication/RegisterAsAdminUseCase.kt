package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {

   suspend fun execute(adminUser: User) {
         if (
            userValidator. isUsernameNotValid(adminUser.name) ||
            userValidator. isPasswordNotValid(adminUser.password) ||
            userValidator.isTypeNotAdmin(adminUser.type) ||
            userValidator.userNameExist(adminUser.name)
        ) {
            throw Exception("Cannot Register!")
        }

        val hashedPassword = passwordHashing.hash(adminUser.password)
        val adminUserWithHashedPassword = adminUser.copy(password = hashedPassword)

        userRepository.createUser(adminUserWithHashedPassword)

    }


}