package logic.use_cases.authentication

import data.authentication.utils.PasswordHashing
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.entities.UserType
import logic.repositories.UserRepository
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class RegisterAsAdminUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordHashing: PasswordHashing
    private lateinit var userValidator: UserValidator
    private lateinit var registerAsAdminUseCase: RegisterAsAdminUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        passwordHashing = PasswordHashing()
        userValidator = mockk()
        registerAsAdminUseCase = RegisterAsAdminUseCase(userRepository, passwordHashing, userValidator)
    }

    @Test
    fun `should throw exception when username is empty`() {
        runTest {
            //Given
            val userName = ""
            val password = "ABCabc123@#4"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns true
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }
    }


    @Test
    fun `should throw exception when username is only spaces`() {
        runTest {
            //Given
            val userName = "      "
            val password = "ABCabc123@#4"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns true
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }
    }


    @Test
    fun `should throw exception when password less than 8 characters`() {
        runTest {
            //Given
            val userName = "mohamed"
            val password = "ABCabc"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns true
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }
    }

    @Test
    fun `should throw exception when password more than 20 characters`() {
        runTest {
            //Given
            val userName = "ahmed ali"
            val password = "1@Abcdefghijklmnopqrstuvwxyz"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)

            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns true
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false


            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }

    }

        @Test
    fun `should throw exception when user type isn't admin`() {
        runTest {
            //Given
            val userName = "ahmed ali"
            val password = "1@Abcdefg"
            val userType = UserType.MateUser("")
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns true
            coEvery { userValidator.userNameExist(any()) } returns false

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }
    }

        @Test
    fun `should throw when username already exist`() {
        runTest {
            //Given
            val userName = "mohamed"
            val password = "ABCabc123@#4"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns true

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }
        }
    }

    @Test
    fun `should return true when create user success`() {
        runTest {
            //Given
            val userName = "mohamed"
            val password =  "ABCabc123@#4"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false
           // when
            registerAsAdminUseCase.execute(user)
            //then
            coVerify(exactly = 1) { userRepository.createUser(any())  }
        }

    }

    ///
    @Test
    fun `throw exception when create user failed`() {
        runTest {
            //Given
            val userName = "mohamed"
            val password = "ABCabc123@#4"
            val userType = UserType.AdminUser
            val hashPassword = passwordHashing.hash(password)
            val user = User(name = userName, password = hashPassword, type = userType)
            coEvery { userValidator.isUsernameNotValid(any()) } returns false
            coEvery { userValidator.isPasswordNotValid(any()) } returns false
            coEvery { userValidator.isTypeNotAdmin(any()) } returns false
            coEvery { userValidator.userNameExist(any()) } returns false
            coEvery { userRepository.createUser(any()) } throws Exception()

            //then
            assertThrows<Exception> {  registerAsAdminUseCase.execute(user) }

        }

    }


}