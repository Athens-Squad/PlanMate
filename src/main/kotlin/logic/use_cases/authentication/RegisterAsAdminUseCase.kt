package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing
) {

    fun execute(adminUser: User): Result<Unit> {
        return if (
            isUsernameNotValid(adminUser.name) ||
            isPasswordNotValid(adminUser.password) ||
            isTypeNotAdmin(adminUser.type) ||
            userNameExist(adminUser.name)
        ) {
            Result.failure(Exception("Cannot Register!"))
        } else {
            runCatching {
                val hashedPassword = passwordHashing.hash(adminUser.password)
                val adminUserWithHashedPassword = adminUser.copy(password = hashedPassword)
                userRepository.createUser(adminUserWithHashedPassword).getOrThrow()
            }
        }
    }

    private fun isUsernameNotValid(userName: String) = userName.isEmpty() || userName.trim().isEmpty()

    private fun isPasswordNotValid(password: String) = password.length < 8 || password.length > 20

    private fun isTypeNotAdmin(userType: UserType) = userType is UserType.MateUser

    private fun userNameExist(userName: String) = userRepository.getUserByUsername(userName).isSuccess
}