package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repositories.UserRepository
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.authentication.RegisterAsMateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class RegisterAsMateUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var registerAsMateUseCase: RegisterAsMateUseCase

    @BeforeEach
    fun setup() {
        userRepository= mockk(relaxed = true)
        registerAsMateUseCase = RegisterAsMateUseCase(userRepository)
    }


    @Test
    fun `should return true when create user success and get user by user name failed`(){
        //Given
        val user = User(name = "mohamed" , password = "ABCabc123@#4" , type = UserType.MateUser("mohamed"))
        every { userRepository.createUser(user) } returns Result.success(Unit)
        every { userRepository.getUserByUsername(user.name) } returns Result.failure(Exception())
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute).isEqualTo(Result.success(Unit))
    }

    @Test
    fun `execute throw exception when create user failed and get user by username failed`(){
        //Given
        val user = User(name = "mohamed" , password = "ABCabc123@#4" , type = UserType.MateUser("mohamed"))
        every { userRepository.createUser(user) } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(user.name) } returns  Result.failure(Exception())

        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)
    }


    @Test
    fun `should throw when username name already exist`(){
        //Given
        val user = User(name = "mohamed" , password = "ABCabc123@#4" , type = UserType.MateUser("mohamed"))
        every { userRepository.getUserByUsername(user.name).isSuccess } returns true
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }


    @Test
    fun `should throw exception when username is empty`(){
        //Given
        val user  =User(
            name = "",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when username is only spaces`(){
        //Given
        val user  =User(
            name = "      ",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when username contain Symbols`(){
        //Given
        val user  =User(
            name = "ahmed @ali $",
            password = "absc3223@A",
            type = UserType.MateUser("ali")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when password less than 8 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "agr",
            type = UserType.MateUser("ali")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when password more than 20 character`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "1@Abcdefghijklmnopqrstuvwxyz",
            type = UserType.MateUser("ali")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }


    @Test
    fun `should throw exception when user type isn't MateUser`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.AdminUser
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when admin Id is empty`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }

    @Test
    fun `should throw exception when admin Id is only space`(){
        //Given
        val user  =User(
            name = "ahmed ali",
            password = "aAB@1AB2AB",
            type = UserType.MateUser("     ")
        )
        //when
        val execute = registerAsMateUseCase.execute(user)
        //then
        assertThat(execute.exceptionOrNull()).isInstanceOf(Exception::class.java)

    }


}