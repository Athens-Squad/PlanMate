package net.thechance.logic.validators.uservalidation

import logic.entities.UserType
import logic.repositories.UserRepository
import net.thechance.logic.exceptions.*

class UserValidatorImpl(
    private val userRepository: UserRepository
) : UserValidator {
    override suspend fun isUsernameNotValid(username: String): Boolean {
        return when{
            username.isEmpty() || username.trim().isEmpty() -> throw InvalidUsernameException()
             else->{false}
        }

    }

    override suspend fun isPasswordNotValid(password: String): Boolean {
        return when {
            password.length < 8 || password.length > 20->throw InvalidPasswordException()
             else->{false}

        }
    }

    override suspend fun isTypeNotAdmin(userType: UserType): Boolean {
        return  when{
            userType is UserType.MateUser ->throw NotAnAdminUserException()

            else->{true}


        }
    }

    override suspend fun isTypeNotMate(userType: UserType): Boolean {
        return when{

            userType is UserType.AdminUser->throw NotAnMateUserException()

            else->{true}


        }
    }

    override suspend fun isMateAdminIdNotValid(userType: UserType): Boolean {
        return when{
            userType is UserType.MateUser && userType.adminName.trim().isEmpty()->throw InvalidUsernameException()

            else->{true}
        }


    }

    override suspend fun userNameExist(username: String): Boolean {
        return try {
            userRepository.getUserByUsername(username)
            throw UserRegistrationException("Registration failed: Username '$username' is already taken.")

        } catch (e: Exception) {
            false
        }
    }



}