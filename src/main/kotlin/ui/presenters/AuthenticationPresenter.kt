@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.presenters

import logic.entities.User
import logic.entities.UserType
import logic.use_cases.authentication.AuthenticationUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.core.Presenter
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.AuthenticationOptions
import kotlin.uuid.ExperimentalUuidApi

class AuthenticationPresenter(
    private val authUseCases: AuthenticationUseCases,
    private val session: UserSession,
    private val consoleIO: ConsoleIO,
    private val adminPresenter: AdminPresenter,
    private val matePresenter: MatePresenter
) : Presenter {
    override suspend fun start() {
        consoleIO.printer.printOptions(AuthenticationOptions.entries)
        val option = consoleIO.reader.readNumberFromUser()
        when (option) {
            AuthenticationOptions.LOGIN.optionNumber -> handleLogin()
            AuthenticationOptions.REGISTER_AS_ADMIN.optionNumber -> handleRegistration()
            else -> {
                consoleIO.printer.printText("Invalid Option.", TextStyle.ERROR)
                start()
            }
        }
    }

    private suspend fun handleLogin() {
        val name = receiveUserInfo("Enter Username:")
        val pass = receiveUserInfo("Enter Password:")
        try {
            session.currentUser = authUseCases.loginUseCase.execute(name, pass)
            consoleIO.printer.printText("Login successful!", TextStyle.SUCCESS)
            if (session.currentUser.type is UserType.AdminUser)
                adminPresenter.start()
            else
                matePresenter.start()
        } catch (e: Exception) {
            consoleIO.printer.printText("Error: ${e.message}", TextStyle.ERROR)
            start()
        }
    }

    private suspend fun handleRegistration() {
        val name = receiveUserInfo("Enter Username:")
        val pass = receiveUserInfo("Enter Password:")
        try {
            authUseCases.registerAsAdminUseCase.execute(
                User(
                    name = name,
                    type = UserType.AdminUser
                ), pass
            )
            consoleIO.printer.printText("Registration successful!", TextStyle.SUCCESS)
            handleLogin()
        } catch (e: Exception) {
            consoleIO.printer.printText("Registration failed: ${e.message}", TextStyle.ERROR)
            start()
        }
    }

    private fun receiveUserInfo(message: String): String {
        consoleIO.printer.printText(message)
        return consoleIO.reader.readStringFromUser()
    }
}