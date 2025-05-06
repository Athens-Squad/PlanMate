package ui.featuresui

import kotlinx.coroutines.*
import logic.entities.User
import logic.use_cases.authentication.AuthenticationUseCases
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.AuthenticationOptions
import ui.io.ConsoleIO


class AuthenticationUi(
    private val consoleIO: ConsoleIO,
    private val authenticationUseCases: AuthenticationUseCases,
    private val userSession: UserSession
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printError("Unexpected error: ${throwable.message}")
    }

    private val authScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

  fun runAuthenticationUi(navigateAfterLoggedInSuccessfully: () -> Unit) {
        consoleIO.printer.printTitle("Select your option (1 or 2) : ")
        consoleIO.printer.printOptions(AuthenticationOptions.entries)

        try {
            val userInput = consoleIO.reader.readNumberFromUser()
            when (userInput) {
                AuthenticationOptions.LOGIN.optionNumber -> handleLogin (navigateAfterLoggedInSuccessfully)

                AuthenticationOptions.REGISTER_AS_ADMIN.optionNumber -> handleRegisterAndLogin(navigateAfterLoggedInSuccessfully)

                else -> {
                    consoleIO.printer.printError("Invalid Input, Please Try Again!")
                    runAuthenticationUi { navigateAfterLoggedInSuccessfully() }
                }
            }
        } catch (invalidInputException: Exception) {
            consoleIO.printer.printError("Invalid Input, Please Try Again!")
            runAuthenticationUi { navigateAfterLoggedInSuccessfully() }
        }
    }

    private fun handleLogin(navigate: () -> Unit) {
        authScope.launch {
            try {
                val user = login()
                userSession.currentUser = user
                consoleIO.printer.printCorrectOutput("Logged in Successfully.")
                navigate()
            } catch (e: Exception) {
                handleException(e)
                runAuthenticationUi(navigate)
            }
        }
    }

    private fun handleRegisterAndLogin(navigate: () -> Unit) {
        authScope.launch {
            try {
                registerAdmin()
                consoleIO.printer.printCorrectOutput("Registered Successfully.")
                handleLogin(navigate)
            } catch (e: Exception) {
                handleException(e)
                runAuthenticationUi(navigate)
            }
        }
    }



    private fun handleException(exception: Throwable) {
        consoleIO.printer.printError(exception.message.toString())
    }

    private suspend fun login(): User {
        consoleIO.printer.printTitle("Login, Please Enter Your Info : ")
        val userName = receiveUserInfo("Enter Your Username : ")
        val password = receiveUserInfo("Enter Your Password : ")

        return authenticationUseCases.loginUseCase.execute(userName, password)
    }

    private suspend fun registerAdmin() {
        consoleIO.printer.printTitle("Signup, Please Enter Your Info : ")
        val userName = receiveUserInfo("Enter Your Username : ")
        val password = receiveUserInfo("Enter Your Password : ")

        return authenticationUseCases.registerAsAdminUseCase.execute(
            User(
                name = userName,
                password = password,
                type = UserType.AdminUser
            )
        )
    }

    fun createMate(adminName: String) {
        consoleIO.printer.printTitle("Create Mate Account, Please Enter Mate's Info : ")
        val userName = receiveUserInfo("Enter Mate's Username : ")
        val password = receiveUserInfo("Enter Mate's Password : ")


        authScope.launch {
            try {
                authenticationUseCases.registerAsMateUseCase.execute(
                    User(
                        name = userName,
                        password = password,
                        type = UserType.MateUser(adminName)
                    )
                )
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }
    }

   private  fun receiveUserInfo(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }
}