@file:OptIn(ExperimentalUuidApi::class)

package ui.featuresui

import kotlinx.coroutines.*
import logic.entities.User
import logic.use_cases.authentication.AuthenticationUseCases
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.AuthenticationOptions
import net.thechance.ui.utils.TextStyle
import ui.io.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi


class AuthenticationUi(
    private val consoleIO: ConsoleIO,
    private val authenticationUseCases: AuthenticationUseCases,
    private val userSession: UserSession
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printText("Unexpected error: ${throwable.message}", TextStyle.ERROR)
    }

    private val authScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    fun runAuthenticationUi(navigateAfterLoggedInSuccessfully: () -> Unit) {
        consoleIO.printer.printText("Select your option (1 or 2) : ", TextStyle.TITLE)
        consoleIO.printer.printOptions(AuthenticationOptions.entries)

        try {
            val userInput = consoleIO.reader.readNumberFromUser()
            when (userInput) {
                AuthenticationOptions.LOGIN.optionNumber -> handleLogin(navigateAfterLoggedInSuccessfully)

                AuthenticationOptions.REGISTER_AS_ADMIN.optionNumber -> handleRegisterAndLogin(
                    navigateAfterLoggedInSuccessfully
                )

                else -> {
                    consoleIO.printer.printText("Invalid Input, Please Try Again!", TextStyle.ERROR)
                    runAuthenticationUi { navigateAfterLoggedInSuccessfully() }
                }
            }
        } catch (invalidInputException: Exception) {
            consoleIO.printer.printText("Invalid Input, Please Try Again!", TextStyle.ERROR)
            runAuthenticationUi { navigateAfterLoggedInSuccessfully() }
        }
    }

    private fun handleLogin(navigate: () -> Unit) {
        authScope.launch {
            try {
                val user = login()
                userSession.currentUser = user
                consoleIO.printer.printText("Logged in Successfully.", TextStyle.SUCCESS)
                navigate()
            } catch (e: Exception) {
                handleException(e)
                runAuthenticationUi(navigate)
            }
        }
    }

    private suspend fun login(): User {
        consoleIO.printer.printText("Login, Please Enter Your Info : ", TextStyle.TITLE)
        val userName = receiveUserInfo("Enter Your Username : ")
        val password = receiveUserInfo("Enter Your Password : ")

        return authenticationUseCases.loginUseCase.execute(userName, password)
    }

    private fun handleRegisterAndLogin(navigate: () -> Unit) {
        authScope.launch {
            try {
                registerAdmin()
                consoleIO.printer.printText("Registered Successfully.", TextStyle.SUCCESS)
                handleLogin(navigate)
            } catch (e: Exception) {
                handleException(e)
                runAuthenticationUi(navigate)
            }
        }
    }

    private fun handleException(exception: Throwable) {
        consoleIO.printer.printText(exception.message.toString(), TextStyle.ERROR)
    }

    private suspend fun registerAdmin() {
        consoleIO.printer.printText("Signup, Please Enter Your Info : ", TextStyle.TITLE)
        val userName = receiveUserInfo("Enter Your Username : ")
        val password = receiveUserInfo("Enter Your Password : ")

        return authenticationUseCases.registerAsAdminUseCase.execute(
            adminUser = User(
                name = userName,
                type = UserType.AdminUser
            ),
            password = password
        )
    }

    fun createMate() {
        consoleIO.printer.printText("Create Mate Account, Please Enter Mate's Info : ", TextStyle.TITLE)
        val userName = receiveUserInfo("Enter Mate's Username : ")
        val password = receiveUserInfo("Enter Mate's Password : ")


        authScope.launch {
            try {
                authenticationUseCases.registerAsMateUseCase.execute(
                    mateUser = User(
                        name = userName,
                        type = UserType.MateUser(userSession.currentUser.name)
                    ),
                    password = password
                )
                consoleIO.printer.printText("Mate Created Successfully!", TextStyle.SUCCESS)
            } catch (exception: Exception) {
                consoleIO.printer.printText("Error : ${exception.message}", TextStyle.ERROR)
            }
        }
    }

    private fun receiveUserInfo(message: String): String {
        consoleIO.printer.printText(message, TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }
}