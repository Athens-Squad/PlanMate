package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import net.thechance.logic.exceptions.AdminUsernameAlreadyExistsException
import net.thechance.logic.exceptions.InvalidPasswordException
import net.thechance.logic.exceptions.InvalidUsernameException
import net.thechance.logic.exceptions.NotAnAdminUserException
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing,
    private val userValidator: UserValidator

) {

   suspend fun execute(adminUser: User) {
       when {
           userValidator.isUsernameNotValid(adminUser.name) -> {
               throw InvalidUsernameException()
           }

           userValidator.isPasswordNotValid(adminUser.password) -> {
               throw InvalidPasswordException()
           }

           userValidator.isTypeNotAdmin(adminUser.type) -> {
               throw NotAnAdminUserException()
           }

           userValidator.userNameExist(adminUser.name) -> {
               throw AdminUsernameAlreadyExistsException()
           }
       }


        val hashedPassword = passwordHashing.hash(adminUser.password)
        val adminUserWithHashedPassword = adminUser.copy(password = hashedPassword)

        userRepository.createUser(adminUserWithHashedPassword)

    }


}