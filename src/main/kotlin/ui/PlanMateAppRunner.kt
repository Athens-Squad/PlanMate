package net.thechance.ui

import kotlinx.coroutines.runBlocking
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.presenters.AuthenticationPresenter

class PlanMateAppRunner(
    private val consoleIO: ConsoleIO,
    private val authenticationPresenter: AuthenticationPresenter
) {
    fun run() {
        consoleIO.printer.printText("Welcome to Athens Plan Mate...", TextStyle.WELCOME)
        runBlocking {
            authenticationPresenter.start()
        }
    }
}