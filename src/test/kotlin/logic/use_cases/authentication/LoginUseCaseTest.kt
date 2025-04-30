package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import helper.createUser
import io.mockk.every
import io.mockk.mockk
import net.thechance.logic.repositories.AuthenticationRepository
import net.thechance.logic.use_cases.authentication.HashPasswordUseCase
import net.thechance.logic.use_cases.authentication.LoginUseCase
import net.thechance.logic.use_cases.authentication.exceptions.InvalidCredentialsException
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class LoginUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var hashPasswordUseCase: HashPasswordUseCase
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)
        hashPasswordUseCase = HashPasswordUseCase()
        loginUseCase = LoginUseCase(authenticationRepository, hashPasswordUseCase)

    }

    @Test
    fun `should login successfully when correct username and password are given`() {
        val username = "Malak"
        val password = "123Password"
        val hashedPassword = hashPasswordUseCase.execute((password))
        val expectedUser = createUser()

        every { authenticationRepository.login(username = username, password = hashedPassword) } returns Result.success(
            expectedUser
        )

        val result = loginUseCase.execute(username, password)


        assertThat(result).isEqualTo(Result.success(expectedUser))

    }

    @Test
    fun `login should fail when incorrect password is given`() {
        val username = "Malak"
        val password = "1234Password"
        val hashedPassword = hashPasswordUseCase.execute((password))

        every { authenticationRepository.login(username = username, password = hashedPassword) } returns Result.failure(
            InvalidCredentialsException()
        )

        val result = loginUseCase.execute(username, password)


        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidCredentialsException::class.java)
    }

    @Test
    fun `login should fail when incorrect username is given (user not found)`() {
        val username = "Malak1"
        val password = "123Password"
        val hashedPassword = hashPasswordUseCase.execute((password))

        every { authenticationRepository.login(username = username, password = hashedPassword) } returns Result.failure(
            InvalidCredentialsException()
        )

        val result = loginUseCase.execute(username, password)


        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidCredentialsException::class.java)
    }

    @Test
    fun `login should fail when username field is empty`() {
        val username = ""
        val password = "123Password"
        val hashedPassword = hashPasswordUseCase.execute((password))

        every { authenticationRepository.login(username = username, password = hashedPassword) } returns Result.failure(
            InvalidCredentialsException()
        )

        val result = loginUseCase.execute(username, password)


        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidCredentialsException::class.java)
    }

    @Test
    fun `login should fail when password field is empty`() {
        val username = "Malak"
        val password = ""
        val hashedPassword = hashPasswordUseCase.execute((password))

        every { authenticationRepository.login(username = username, password = hashedPassword) } returns Result.failure(
            InvalidCredentialsException()
        )

        val result = loginUseCase.execute(username, password)


        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidCredentialsException::class.java)
    }
}
