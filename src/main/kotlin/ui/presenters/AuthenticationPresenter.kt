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
    private val consoleIo: ConsoleIO,
    private val adminPresenter: AdminPresenter,
    private val matePresenter: MatePresenter
) : Presenter {
    override suspend fun start() {
        consoleIo.printer.printOptions(AuthenticationOptions.entries)
        val option = consoleIo.reader.readNumberFromUser()
        when (option) {
            AuthenticationOptions.LOGIN.optionNumber -> handleLogin()
            AuthenticationOptions.REGISTER_AS_ADMIN.optionNumber -> handleRegistration()
            else -> {
                consoleIo.printer.printText("Invalid Option.", TextStyle.ERROR)
                start()
            }
        }
    }

    private suspend fun handleLogin() {
        val name = prompt("Enter Username:")
        val pass = prompt("Enter Password:")
        try {
            session.currentUser = authUseCases.loginUseCase.execute(name, pass)
            consoleIo.printer.printText("Login successful!", TextStyle.SUCCESS)
            if (session.currentUser.type is UserType.AdminUser)
                adminPresenter.start()
            else
                matePresenter.start()
        } catch (e: Exception) {
            consoleIo.printer.printText("Error: ${e.message}", TextStyle.ERROR)
            start()
        }
    }

    private suspend fun handleRegistration() {
        val name = prompt("Enter Username:")
        val pass = prompt("Enter Password:")
        try {
            authUseCases.registerAsAdminUseCase.execute(
                User(
                    name = name,
                    type = UserType.AdminUser
                ), pass
            )
            consoleIo.printer.printText("Registration successful!", TextStyle.SUCCESS)
            handleLogin()
        } catch (e: Exception) {
            consoleIo.printer.printText("Registration failed: ${e.message}", TextStyle.ERROR)
            start()
        }
    }

    private fun prompt(message: String): String {
        consoleIo.printer.printText(message)
        return consoleIo.reader.readStringFromUser()
    }
}