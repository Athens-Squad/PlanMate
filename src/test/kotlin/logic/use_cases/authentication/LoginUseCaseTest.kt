package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.exceptions.InvalidCredentialsException
import logic.repositories.AuthenticationRepository
import net.thechance.logic.validators.uservalidation.UserValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var passwordHashing: PasswordHashing
    private lateinit var userValidator: UserValidator

    @BeforeTest
    fun setUp() {
        authenticationRepository = mockk()
        passwordHashing = mockk()
        userValidator = mockk()

        loginUseCase = LoginUseCase(
            authenticationRepository = authenticationRepository,
            passwordHashing = passwordHashing,
            userValidator = userValidator
        )
    }

    @Test
    fun `should throw InvalidCredentialsException when username is invalid`() = runTest {
        // Given
        val username = "invalidUser"
        val password = "password123"

        coEvery { userValidator.isUsernameNotValid(username) } returns true
        coEvery { userValidator.isPasswordNotValid(password) } returns false

        // When & Then
        assertThrows<InvalidCredentialsException> {
            loginUseCase.execute(username, password)
        }
    }


    @Test
    fun `should throw InvalidCredentialsException when password is invalid`() = runTest {
        // Given
        val username = "validUser"
        val password = "short"

        coEvery { userValidator.isUsernameNotValid(username) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns true

        // When & Then
        assertThrows<InvalidCredentialsException> {
            loginUseCase.execute(username, password)
        }
    }

    @Test
    fun `should call authenticationRepository login when credentials are valid`() = runTest {
        // Given
        val username = "validUser"
        val password = "password123"
        val hashedPassword = "hashedPassword"
        val user = mockk<User>()

        coEvery { userValidator.isUsernameNotValid(username) } returns false
        coEvery { userValidator.isPasswordNotValid(password) } returns false
        coEvery { passwordHashing.hash(password) } returns hashedPassword
        coEvery { authenticationRepository.login(username, hashedPassword) } returns user

        // When
        val result = loginUseCase.execute(username, password)

        // Then
        assertEquals(user, result)
    }
}