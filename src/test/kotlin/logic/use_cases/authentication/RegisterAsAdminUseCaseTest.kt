package logic.use_cases.authentication

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.authentication.RegisterAsAdminUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RegisterAsAdminUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var registerAsAdminUseCase: RegisterAsAdminUseCase

    @BeforeEach
    fun setup() {
        userRepository= mockk(relaxed = true)
        registerAsAdminUseCase = RegisterAsAdminUseCase(userRepository)
    }

    @Test
    fun `execute should create user when called`(){
        //Given
        val user : User = mockk()
        //when
        registerAsAdminUseCase.execute(user)
        //then
        verify { userRepository.createUser(user) }
    }

    @Test
    fun `execute should return true when create user success`(){
        //Given
        val user : User = mockk()
        every { userRepository.createUser(any()) } returns Result.success(Unit)
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertTrue { execute }
    }

    @Test
    fun `execute throw exception when create user failed`(){
        //Given
        val user : User = mockk()
        every { userRepository.createUser(any()) } returns Result.failure(Exception())
        //when & then
        assertThrows<Exception> {
            val execute = registerAsAdminUseCase.execute(user)

        }
    }

    @Test
    fun `execute should get user by username when called`(){
        //Given
        val user : User = mockk()
        //when
        registerAsAdminUseCase.execute(user)
        //then
        verify { userRepository.getUserByUsername(user.name) }
    }

    @Test
    fun `execute throw when username name founded before`(){
        //Given
        val user : User = mockk()
        every { userRepository.getUserByUsername(any()) } returns Result.success(user)
        //when & then
        assertThrows<Exception> {
            val execute = registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should return true when user name not founded before`(){
        //Given
        val user : User = mockk()
        every { userRepository.getUserByUsername(any()) } returns Result.failure(Exception())
        //when
        val execute = registerAsAdminUseCase.execute(user)
        //then
        assertTrue { execute }
    }



    @Test
    fun `execute should throw exception when username is empty`(){
        //Given
        val user  =User(
            name = "",
            password = "absc3223@A",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when username is only spaces`(){
        //Given
        val user  =User(
            name = "      ",
            password = "absc3223@A",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when username contain Symbols`(){
        //Given
        val user  =User(
            name = "ahmed @ali $",
            password = "absc3223@A",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password less than 8 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "agr",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password more than 20 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1@Abcdefghijklmnopqrstuvwxyz",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain capital letters`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1ab#abab",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain small letters`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1AB#ABAB",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain numbers`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB#ABAB",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain symbols`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB1AB2AB",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when user type isn't admin`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("")
        )
        //when & then
        assertThrows<Exception> {
            registerAsAdminUseCase.execute(user)
        }
    }


}