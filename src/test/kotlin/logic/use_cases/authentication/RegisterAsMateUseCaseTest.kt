package logic.use_cases.authentication

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.authentication.RegisterAsMateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertTrue

class RegisterAsMateUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var registerAsMateUseCase: RegisterAsMateUseCase

    @BeforeEach
    fun setup() {
        userRepository= mockk(relaxed = true)
        registerAsMateUseCase = RegisterAsMateUseCase(userRepository)
    }

    @Test
    fun `execute should create user when called`(){
        //Given
        val user : User = mockk()
        //when
        registerAsMateUseCase.execute(user)
        //then
        verify { userRepository.createUser(user) }
    }

    @Test
    fun `execute should return true when create user success`(){
        //Given
        val user : User = mockk()
        every { userRepository.createUser(any()) } returns Result.success(Unit)
        //when
        val execute = registerAsMateUseCase.execute(user)
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
            val execute = registerAsMateUseCase.execute(user)

        }
    }

    @Test
    fun `execute should get user by username when called`(){
        //Given
        val user : User = mockk()
        //when
        registerAsMateUseCase.execute(user)
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
            val execute = registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should return true when user name not founded before`(){
        //Given
        val user : User = mockk()
        every { userRepository.getUserByUsername(any()) } returns Result.failure(Exception())
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertTrue { execute }
    }



    @Test
    fun `execute should throw exception when username is empty`(){
        //Given
        val user  =User(
            name = "",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when username is only spaces`(){
        //Given
        val user  =User(
            name = "      ",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when username contain Symbols`(){
        //Given
        val user  =User(
            name = "ahmed @ali $",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password less than 8 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "agr",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password more than 20 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1@Abcdefghijklmnopqrstuvwxyz",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain capital letters`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1ab#abab",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain small letters`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1AB#ABAB",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain numbers`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB#ABAB",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when password not contain symbols`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB1AB2AB",
            type = UserType.MateUser("ali")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when user type isn't MateUser`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.AdminUser
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when admin Id is empty`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when admin Id is only space`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("     ")
        )
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should throw exception when admin Id not founded`(){
        //Given
        val userType = UserType.MateUser("ali")
        val user : User  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = userType
        )

        every { userRepository.getUserByUsername(userType.adminId) } returns Result.failure(Exception())
        //when & then
        assertThrows<Exception> {
            registerAsMateUseCase.execute(user)
        }
    }

    @Test
    fun `execute should return true when admin Id founded`(){
        //Given
        val userType = UserType.MateUser("ali")
        val user : User  = mockk()
        every { userRepository.getUserByUsername(userType.adminId) } returns Result.success(user)

        //when
        val execute = registerAsMateUseCase.execute(user)

        //then
        assertTrue { execute }
    }
}