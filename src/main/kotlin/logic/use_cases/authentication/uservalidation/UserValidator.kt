package net.thechance.logic.use_cases.authentication.uservalidation

import logic.entities.UserType

interface UserValidator {

    fun isUsernameNotValid(username: String): Boolean
    fun isPasswordNotValid(password: String): Boolean
    fun isTypeNotAdmin(userType: UserType): Boolean
    fun isTypeNotMate(userType: UserType): Boolean
    fun isMateAdminIdNotValid(userType: UserType): Boolean
    fun userNameExist(username: String): Boolean

}