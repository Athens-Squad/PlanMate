package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repositories.UserRepository
import data.authentication.utils.PasswordHashing
import logic.entities.UserType
import logic.use_cases.authentication.RegisterAsAdminUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class RegisterAsAdminUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordHashing: PasswordHashing
    private lateinit var registerAsAdminUseCase: RegisterAsAdminUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        passwordHashing = PasswordHashing()
        registerAsAdminUseCase = RegisterAsAdminUseCase(userRepository,passwordHashing)
    }

    @Test
    fun `should return true when create user success and get user by user name failed`() {
        //Given
        val user = User(name = "mohamed", password = "ABCabc123@#4", type = UserType.AdminUser)
        every { userRepository.createUser(user) } returns Result.success(Unit)
        every { userRepository.getUserByUsername(user.name) } returns Result.failure(Exception())
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute).isEqualTo(Result.success(Unit))
    }

    ///
    @Test
    fun `throw exception when create user failed and get user by username failed`() {
        //Given
        val user = User(name = "mohamed", password = "ABCabc123@#4", type = UserType.AdminUser)
        every { userRepository.createUser(user.copy(password = passwordHashing.hash(user.password))) } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(user.name) } returns Result.failure(Exception())
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }

    @Test
    fun `should throw when username already exist`() {
        //Given
        val user = User(name = "mohamed", password = "ABCabc123@#4", type = UserType.AdminUser)
        every { userRepository.getUserByUsername(user.name).isSuccess } returns true
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }

    @Test
    fun `should throw exception when username is empty`() {
        //Given
        val user = User(
            name = "",
            password = "absc3223@A",
            type = UserType.AdminUser
        )
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }


    @Test
    fun `should throw exception when username is only spaces`() {
        //Given
        val user = User(
            name = "      ",
            password = "absc3223@A",
            type = UserType.AdminUser
        )
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }


    @Test
    fun `should throw exception when password less than 8 characters`() {
        //Given
        val user = User(
            name = "ahmed ali",
            password = "agr",
            type = UserType.AdminUser
        )
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }

    @Test
    fun `should throw exception when password more than 20 characters`() {
        //Given
        val user = User(
            name = "ahmed ali",
            password = "1@Abcdefghijklmnopqrstuvwxyz",
            type = UserType.AdminUser
        )
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }

    @Test
    fun `should throw exception when user type isn't admin`() {
        //Given
        val user = User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("")
        )
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }


}