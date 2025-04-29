package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType

class RegisterAsMateUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(mateUser: User): Boolean {
        if (
            mateUser.name.isEmpty() ||
            mateUser.name.trim().isEmpty()
        ) {
            throw Exception()
        }

        if (
            mateUser.password.length < 8 ||
            mateUser.password.length > 20 ||
            isValidPassword(mateUser.password)
        ) {
            throw Exception()
        }

        if (
            mateUser.type is UserType.AdminUser
        ) {
            throw Exception()
        }

        if( (mateUser.type as UserType.MateUser).adminId.trim().isEmpty() ){
            throw Exception()
        }

        val x = userRepository.getUserByUsername(mateUser.name)
        if (x.isSuccess) {
            throw Exception()
        }

        if (userRepository.createUser(mateUser).isSuccess) {
            return true
        } else {
            throw Exception()
        }

    }

    private fun isValidPassword(password: String): Boolean {
        var hasCapitalChar = false
        var hasSmallChar = false
        var hasNumbers = false
        var hasSymbols = false
        password.forEach {
            hasCapitalChar = isCapitalChar(it) || hasCapitalChar
            hasSmallChar = isSmallChar(it) || hasSmallChar
            hasNumbers = isNumbers(it) || hasNumbers
            hasSymbols = checkCharIsSymbol(hasNumbers, hasCapitalChar, hasSmallChar) || hasSymbols
        }
        return (hasCapitalChar && hasSmallChar && hasNumbers && hasSymbols)
    }

    private fun isCapitalChar(char: Char): Boolean {
        return char in 'A'..'Z'
    }

    private fun isSmallChar(char: Char): Boolean {
        return char in 'a'..'z'
    }

    private fun isNumbers(char: Char): Boolean {
        return char in '0'..'9'
    }

    private fun checkCharIsSymbol(isNumber: Boolean, isCapitalChar: Boolean, isSmallChar: Boolean): Boolean {
        return !(isNumber || isCapitalChar || isSmallChar)
    }
}


