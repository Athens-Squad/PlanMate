package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import data.authentication.utils.PasswordHashing
import helper.authentication_helper.FakeUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

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
            val expectedUser = FakeUser.createUser
            coEvery {  userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery {   authenticationRepository.login(any(), any()) } returns expectedUser

            val result = loginUseCase.execute(expectedUser.name, expectedUser.password)

            assertThat(result).isEqualTo(expectedUser)
        }
    }

    @Test
    fun `login should fail when password is only spaces`() {
        runTest {
            val user = FakeUser.createUser.copy(password="     ")
            coEvery { userValidator.isPasswordNotValid(user.password)} returns true
            coEvery { userValidator.isUsernameNotValid(any()) } returns false

            assertThrows<Exception> { loginUseCase.execute(user.name, user.password) }
        }
    }

    @Test
    fun `login should fail when incorrect username is given (user not found)`() {
        runTest {
            val user = FakeUser.createUser
            coEvery { authenticationRepository.login(username = user.name, password = user.password) } throws
                    UserNotFoundException()

            assertThrows<Exception> { loginUseCase.execute(user.name, user.password) }
        }

    }

    @Test
    fun `login should fail when username field is empty`() {
        runTest {
            val user = FakeUser.createUser.copy(name = "")
            coEvery { userValidator.isUsernameNotValid(user.name) } returns true

            assertThrows<Exception> { loginUseCase.execute(user.name, user.password) }
        }

    }

    @Test
    fun `login should fail when password field is empty`() {
        runTest {
            val user = FakeUser.createUser.copy(password = "")

            coEvery { userValidator.isPasswordNotValid(user.password) } returns true
            coEvery { userValidator.isUsernameNotValid(any())  } returns false

            assertThrows<Exception> { loginUseCase.execute(user.name, user.password) }
        }

    }
}
