package ui.featuresui

import logic.entities.User
import logic.use_cases.authentication.AuthenticationUseCases
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import ui.io.ConsoleIO

class AuthenticationUi(
    private val consoleIO: ConsoleIO,
    private val authenticationUseCases: AuthenticationUseCases,
    private val userSession: UserSession
) {

    fun runAuthenticationUi(navigateAfterLoggedInSuccessfully: () -> Unit) {
        consoleIO.printer.printTitle("Select your option (1 or 2) : ")
        consoleIO.printer.printOption("1 : Login")
        consoleIO.printer.printOption("2 : Register as Admin")

        val userInput = consoleIO.reader.readNumberFromUser()
        when(userInput) {
            1 -> {
                login()
                    .onSuccess {
                        userSession.currentUser = it
                        consoleIO.printer.printCorrectOutput(userSession.currentUser.toString())
                        navigateAfterLoggedInSuccessfully()
                    }
                    .onFailure {
                        handleException(it)
                        runAuthenticationUi(navigateAfterLoggedInSuccessfully)
                    }
            }
            2 -> {
                registerAdmin()
                    .onSuccess {
                        consoleIO.printer.printCorrectOutput("Registered Successfully.")
                        login()
                            .onSuccess {
                                userSession.currentUser = it
                                consoleIO.printer.printCorrectOutput(userSession.currentUser.toString())
                                navigateAfterLoggedInSuccessfully()
                            }
                            .onFailure {
                                handleException(it)
                                runAuthenticationUi(navigateAfterLoggedInSuccessfully)
                            }
                    }
                    .onFailure {
                        handleException(it)
                        runAuthenticationUi(navigateAfterLoggedInSuccessfully)
                    }
            }
        }
    }

    private fun handleException(exception: Throwable) {
        consoleIO.printer.printError(exception.message.toString())
    }

    private fun login(): Result<User> {
        consoleIO.printer.printTitle("Login, Please Enter Your Info : ")
        val userName = receiveUserInfo("Enter Your Username : ")
        val password = receiveUserInfo("Enter Your Password : ")

        return  authenticationUseCases.loginUseCase.execute(userName, password)
    }

    private fun registerAdmin(): Result<Unit> {
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

    private fun createMate(adminId: String): Result<Unit> {
        consoleIO.printer.printTitle("Create Mate Account, Please Enter Mate's Info : ")
        val userName = receiveUserInfo("Enter Mate's Username : ")
        val password = receiveUserInfo("Enter Mate's Password : ")

        return authenticationUseCases.registerAsAdminUseCase.execute(
            User(
                name = userName,
                password = password,
                type = UserType.MateUser(adminId)
            )
        )
    }

    private fun receiveUserInfo(message: String): String {
        consoleIO.printer.printInfoLine(message)
        return consoleIO.reader.readStringFromUser()
    }
}