package logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
    private val passwordHashing: PasswordHashing
) {
    fun execute(mateUser: User): Result<Unit> {
        return if (
            isUsernameNotValid(mateUser.name) ||
            isPasswordNotValid(mateUser.password) ||
            isTypeNotMate(mateUser.type) ||
            isMateAdminIdNotValid(mateUser.type)||
            userNameExist(mateUser.name)
        ) {
            Result.failure(Exception("Cannot Register!"))
        } else {
            runCatching {
                val hashedPassword = passwordHashing.hash(mateUser.password)
                val mateUserWithHashedPassword = mateUser.copy(password = hashedPassword)
                userRepository.createUser(mateUserWithHashedPassword).getOrThrow()
            }
        }

    }

    private fun isUsernameNotValid(userName: String) = userName.isEmpty() || userName.trim().isEmpty()

    private fun isMateAdminIdNotValid(type: UserType) =  (type as UserType.MateUser).adminId.trim().isEmpty()

    private fun isPasswordNotValid(password: String) = password.length < 8 || password.length > 20

    private fun isTypeNotMate(userType: UserType) = userType is UserType.AdminUser

    private fun userNameExist(userName: String) = userRepository.getUserByUsername(userName).isSuccess

}


