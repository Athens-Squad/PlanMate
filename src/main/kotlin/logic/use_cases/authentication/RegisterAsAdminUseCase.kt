package net.thechance.logic.use_cases.authentication

import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType

class RegisterAsAdminUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(adminUser: User): Boolean {
        if (
            adminUser.name.isEmpty() ||
            adminUser.name.trim().isEmpty()
        ) {
            throw Exception()
        }

        if (
            adminUser.password.length < 8 ||
            adminUser.password.length > 20 ||
            isValidPassword(adminUser.password)
        ) {
            throw Exception()
        }

        if (
            adminUser.type is UserType.MateUser
        ) {
            throw Exception()
        }

        val x = userRepository.getUserByUsername(adminUser.name)
        if (x.isSuccess) {
            throw Exception()
        }

        if (userRepository.createUser(adminUser).isSuccess) {
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
