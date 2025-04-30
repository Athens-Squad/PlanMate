package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
) {

    fun execute(adminUser: User): Result<Unit> {
        return if (
            isUsernameNotValid(adminUser.name) ||
            isPasswordNotValid(adminUser.password) ||
            isTypeNotAdmin(adminUser.type) ||
            userNameExist(adminUser.name)
        ) {
            Result.failure(Exception())
        } else {
            runCatching {
                userRepository.createUser(adminUser).getOrThrow()
            }
        }
    }

    private fun isUsernameNotValid(userName: String) = userName.isEmpty() || userName.trim().isEmpty()

    private fun isPasswordNotValid(password: String) = password.length < 8 || password.length > 20

    private fun isTypeNotAdmin(userType: UserType) = userType is UserType.MateUser

    private fun userNameExist(userName: String) = userRepository.getUserByUsername(userName).isSuccess
}