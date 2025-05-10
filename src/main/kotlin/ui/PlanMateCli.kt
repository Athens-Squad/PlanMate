package ui

import kotlinx.coroutines.*
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.handlers.AdminOptionsHandler
import net.thechance.ui.handlers.MateOptionsHandler
import net.thechance.ui.utils.TextStyle
import ui.featuresui.*
import ui.io.ConsoleIO

class PlanMateCli(
    private val consoleIO: ConsoleIO,
    private val session: UserSession,
    private val authenticationUi: AuthenticationUi,
    private val adminOptionsHandler: AdminOptionsHandler,
    private val mateOptionsHandler: MateOptionsHandler
) {
    fun run() {
        consoleIO.printer.printText("Welcome to Athens Plan Mate...",TextStyle.WELCOME)
        authenticationUi.runAuthenticationUi { startAuthenticationUi() }
    }

    private fun startAuthenticationUi() {
        when (session.currentUser.type) {
            UserType.AdminUser -> runBlocking { adminOptionsHandler.handle() }
            is UserType.MateUser -> runBlocking { mateOptionsHandler.handle() }
        }
    }

}