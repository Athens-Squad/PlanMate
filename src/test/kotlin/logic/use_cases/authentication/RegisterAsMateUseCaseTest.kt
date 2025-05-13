@file:OptIn(ExperimentalUuidApi::class)
package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.entities.UserType
import logic.exceptions.InvalidCredentialsException
import logic.repositories.UserRepository
import net.thechance.logic.validators.uservalidation.UserValidator
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class RegisterAsMateUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var passwordHashing: PasswordHashing
    private lateinit var userValidator: UserValidator
    private lateinit var registerAsMateUseCase: RegisterAsMateUseCase

    @BeforeTest
    fun setUp() {
        userRepository = mockk()
        passwordHashing = mockk()
        userValidator = mockk()
        registerAsMateUseCase = RegisterAsMateUseCase(userRepository, passwordHashing, userValidator)
    }

    @Test
    fun `should register mate successfully when input is valid`() = runTest {
        // Given
        val mateUser = User(name = "mate123", type = UserType.MateUser(adminName = "admin1"))
        val password = "validPass123"

        coEvery { userValidator.isUsernameNotValid(mateUser.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotMate(mateUser.type) } returns false
        coEvery { userValidator.isMateAdminIdNotValid(mateUser.type) } returns false
        coEvery { userValidator.userNameExist(mateUser.name) } returns false
        coEvery { passwordHashing.hash(password) } returns "hashedPass"
        coEvery { userRepository.createUser(mateUser, "hashedPass") } returns Unit

        // When
        registerAsMateUseCase.execute(mateUser, password)

        // Then
        coVerify {
            userRepository.createUser(mateUser, "hashedPass")
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when username is invalid`() = runTest {
        val user = User(name = "invalid", type = UserType.MateUser("admin1"))
        val password = "pass"

        coEvery { userValidator.isUsernameNotValid(user.name) } throws InvalidCredentialsException()

        assertFailsWith<InvalidCredentialsException> {
            registerAsMateUseCase.execute(user, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when password is invalid`() = runTest {
        val user = User(name = "valid", type = UserType.MateUser("admin1"))
        val password = "weak"

        coEvery { userValidator.isUsernameNotValid(user.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } throws InvalidCredentialsException()

        assertFailsWith<InvalidCredentialsException> {
            registerAsMateUseCase.execute(user, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when user is not MateUser`() = runTest {
        val user = User(name = "mate", type = UserType.AdminUser)
        val password = "validPass"

        coEvery { userValidator.isUsernameNotValid(user.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotMate(user.type) } throws InvalidCredentialsException()

        assertFailsWith<InvalidCredentialsException> {
            registerAsMateUseCase.execute(user, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when mate admin ID is invalid`() = runTest {
        val user = User(name = "mate", type = UserType.MateUser(""))
        val password = "validPass"

        coEvery { userValidator.isUsernameNotValid(user.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotMate(user.type) } returns false
        coEvery { userValidator.isMateAdminIdNotValid(user.type) } throws InvalidCredentialsException()

        assertFailsWith<InvalidCredentialsException> {
            registerAsMateUseCase.execute(user, password)
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when username already exists`() = runTest {
        val user = User(name = "duplicate", type = UserType.MateUser("admin1"))
        val password = "validPass"

        coEvery { userValidator.isUsernameNotValid(user.name) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { userValidator.isTypeNotMate(user.type) } returns false
        coEvery { userValidator.isMateAdminIdNotValid(user.type) } returns false
        coEvery { userValidator.userNameExist(user.name) } throws InvalidCredentialsException()

        assertFailsWith<InvalidCredentialsException> {
            registerAsMateUseCase.execute(user, password)
        }
    }
}