@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.entities.UserType
import logic.exceptions.InvalidCredentialsException
import logic.repositories.UserRepository
import net.thechance.logic.validators.uservalidation.UserValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class RegisterAsAdminUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val passwordHashing: PasswordHashing = mockk()
    private val userValidator: UserValidator = mockk()

    private val registerAsAdminUseCase = RegisterAsAdminUseCase(userRepository, passwordHashing, userValidator)

    @Test
    fun `should throw InvalidCredentialsException when username is invalid`() = runTest {
        // Given
        val adminUser = User(name = "invalidUser", type = UserType.AdminUser)
        val password = "validPassword"

        coEvery { userValidator.isUsernameNotValid(adminUser.name) } throws InvalidCredentialsException()
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotAdmin(adminUser.type) } returns false
        coEvery { userValidator.userNameExist(adminUser.name) } returns false
        coEvery { userRepository.createUser(any(), any()) } returns Unit

        // Avoid MockKException:
        coEvery { passwordHashing.hash(password) } returns "hashedPassword"

        // When & Then
        assertFailsWith<InvalidCredentialsException> {
            registerAsAdminUseCase.execute(adminUser, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when password is invalid`() = runTest {
        // Given
        val adminUser = User(name = "validUser", type = UserType.AdminUser)
        val password = "short"
        val hashedPassword = "hashedPassword"

        coEvery { userValidator.isUsernameNotValid(adminUser.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } throws InvalidCredentialsException()
        coEvery { userValidator.isTypeNotAdmin(adminUser.type) } returns false
        coEvery { userValidator.userNameExist(adminUser.name) } returns false
        coEvery { passwordHashing.hash(password) } returns hashedPassword
        coEvery { userRepository.createUser(any(), any()) } just runs

        // When & Then
        assertFailsWith<InvalidCredentialsException> {
            registerAsAdminUseCase.execute(adminUser, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when user type is not ADMIN`() = runTest {
        // Given
        val adminUser = User(name = "validUser", type = UserType.MateUser(adminName = "12"))
        val password = "validPassword"
        val hashedPassword = "hashedPassword"

        coEvery { userValidator.isUsernameNotValid(adminUser.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotAdmin(adminUser.type) } throws InvalidCredentialsException()
        coEvery { userValidator.userNameExist(adminUser.name) } returns false
        coEvery { passwordHashing.hash(password) } returns hashedPassword
        coEvery { userRepository.createUser(any(), any()) } just runs

        // When & Then
        assertFailsWith<InvalidCredentialsException> {
            registerAsAdminUseCase.execute(adminUser, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when username already exists`() = runTest {
        // Given
        val adminUser = User(name = "existingUser", type = UserType.AdminUser)
        val password = "validPassword"
        val hashedPassword = "hashedPassword"

        coEvery { userValidator.isUsernameNotValid(adminUser.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotAdmin(adminUser.type) } returns false
        coEvery { userValidator.userNameExist(adminUser.name) } throws InvalidCredentialsException()
        coEvery { passwordHashing.hash(password) } returns hashedPassword
        coEvery { userRepository.createUser(any(), any()) } just runs

        // When & Then
        assertFailsWith<InvalidCredentialsException> {
            registerAsAdminUseCase.execute(adminUser, password)
        }
    }

    @Test
    fun `should call userRepository createUser when all validations pass`() = runTest {
        val adminUser = User(name = "validUser", type = UserType.AdminUser)
        val password = "validPassword"
        val hashedPassword = "hashedPassword"

        coEvery { userValidator.isUsernameNotValid(adminUser.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotAdmin(adminUser.type) } returns false
        coEvery { userValidator.userNameExist(adminUser.name) } returns false
        coEvery { passwordHashing.hash(password) } returns hashedPassword
        coEvery { userRepository.createUser(adminUser, hashedPassword) } just Runs

        assertDoesNotThrow {
            runBlocking {
                registerAsAdminUseCase.execute(adminUser, password)
            }
        }
    }
}