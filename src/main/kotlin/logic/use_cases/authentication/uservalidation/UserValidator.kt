package net.thechance.logic.use_cases.authentication.uservalidation

import logic.entities.UserType

interface UserValidator {

    suspend fun isUsernameNotValid(username: String): Boolean
   suspend fun isPasswordNotValid(password: String): Boolean
   suspend fun isTypeNotAdmin(userType: UserType): Boolean
   suspend fun isTypeNotMate(userType: UserType): Boolean
    suspend fun isMateAdminIdNotValid(userType: UserType): Boolean
   suspend fun userNameExist(username: String): Boolean

}