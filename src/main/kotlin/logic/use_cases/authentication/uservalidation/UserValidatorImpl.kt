package net.thechance.logic.use_cases.authentication.uservalidation

import logic.entities.UserType
import logic.repositories.UserRepository

class UserValidatorImpl (
    private val userRepository: UserRepository
):UserValidator{
    override suspend fun isUsernameNotValid(username: String): Boolean {
      return  username.isEmpty() || username.trim().isEmpty()

    }

    override suspend fun isPasswordNotValid(password: String): Boolean {
        return password.length < 8 || password.length > 20
    }

    override suspend fun isTypeNotAdmin(userType: UserType): Boolean {
         return  userType is UserType.MateUser
    }

    override suspend fun isTypeNotMate(userType: UserType): Boolean {
            return userType is UserType.AdminUser
    }

    override suspend fun isMateAdminIdNotValid(userType: UserType): Boolean {
   return userType is UserType.MateUser && userType.adminName.trim().isEmpty()


    }

    override suspend fun userNameExist(username: String): Boolean {
         return try {
             userRepository.getUserByUsername(username)
             true
         } catch (e: Exception) {
             false
         }
    }
}