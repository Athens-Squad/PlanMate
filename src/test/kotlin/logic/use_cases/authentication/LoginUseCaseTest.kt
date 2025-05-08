package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import helper.createUser
import io.mockk.every
import io.mockk.mockk
import logic.repositories.AuthenticationRepository
import data.authentication.utils.PasswordHashing
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.entities.UserType
import logic.use_cases.authentication.LoginUseCase
import logic.use_cases.authentication.exceptions.InvalidCredentialsException
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LoginUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var passwordHashing: PasswordHashing
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var userValidator: UserValidator

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)
        passwordHashing = PasswordHashing()
        userValidator = mockk()
        loginUseCase = LoginUseCase(authenticationRepository, passwordHashing, userValidator)

    }

    @Test
    fun `should login successfully when correct username and password are given`() {
        runTest {
            val username = "Malak"
            val password = "123Password"
            val hashedPassword = passwordHashing.hash((password))
            val expectedUser = User(
                name = username,
                password = hashedPassword,
                type = UserType.AdminUser
            )
            coEvery {  userValidator.isUsernameNotValid(username) } returns false
            coEvery { userValidator.isPasswordNotValid(password) } returns false
            coEvery {   authenticationRepository.login(username = username, password = hashedPassword) } returns expectedUser

            val result = loginUseCase.execute(username, password)

            assertThat(result.name).isEqualTo(expectedUser.name)
        }
    }

    @Test
    fun `login should fail when incorrect password is given`() {
        runTest {
            val username = "Malak1"
            val password = "   "

            coEvery { userValidator.isPasswordNotValid(password)} returns true
            coEvery { userValidator.isUsernameNotValid(username) } returns false

            assertThrows<Exception> { loginUseCase.execute(username, password) }
        }
    }

    @Test
    fun `login should fail when incorrect username is given (user not found)`() {
        runTest {
            val username = "Malak1"
            val password = "123Password"
            val hashedPassword = passwordHashing.hash((password))

            coEvery { authenticationRepository.login(username = username, password = hashedPassword) } throws
                    InvalidCredentialsException()

            assertThrows<Exception> { loginUseCase.execute(username, password) }
        }

    }

    @Test
    fun `login should fail when username field is empty`() {
        runTest {
            val username = ""
            val password = "123Password"
            val hashedPassword = passwordHashing.hash((password))

            coEvery { userValidator.isUsernameNotValid(username) } returns true

            assertThrows<Exception> { loginUseCase.execute(username, hashedPassword) }
        }

    }

    @Test
    fun `login should fail when password field is empty`() {
        runTest {
            val username = "Malak"
            val password = ""
            val hashedPassword = passwordHashing.hash((password))

            coEvery { userValidator.isPasswordNotValid(password) } returns true
            coEvery { userValidator.isUsernameNotValid(username)  } returns false

            assertThrows<Exception> { loginUseCase.execute(username, hashedPassword) }
        }

    }
}
