package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(mateUser: User): Result<Unit> {
        return if (
            isUsernameNotValid(mateUser.name) ||
            isPasswordNotValid(mateUser.password) ||
            isTypeNotMate(mateUser.type) ||
            isMateAdminIdNotValid(mateUser.type)||
            userNameExist(mateUser.name)
        ) {
            Result.failure(Exception())
        } else {
            runCatching {
                userRepository.createUser(mateUser).getOrThrow()
            }
        }

    }

    private fun isUsernameNotValid(userName: String) = userName.isEmpty() || userName.trim().isEmpty()

    private fun isMateAdminIdNotValid(type: UserType) =  (type as UserType.MateUser).adminId.trim().isEmpty()

    private fun isPasswordNotValid(password: String) = password.length < 8 || password.length > 20

    private fun isTypeNotMate(userType: UserType) = userType is UserType.AdminUser

    private fun userNameExist(userName: String) = userRepository.getUserByUsername(userName).isSuccess

}


