package net.thechance.ui.handlers

import ui.io.ConsoleIO
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.AdminOptions
import ui.featuresui.*

class AdminOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val authenticationUi: AuthenticationUi,
    private val session: UserSession
) {
    fun handle() {
        do {
            consoleIO.printer.printWelcomeMessage("Hello Mr/Ms : ${session.currentUser.name}")

            consoleIO.printer.printOptions(AdminOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                AdminOptions.SHOW_ALL_PROJECTS.optionNumber -> showAllProjects()
                AdminOptions.CREATE_PROJECT.optionNumber -> createProject()
                AdminOptions.CREATE_MATE.optionNumber -> createMate()
                AdminOptions.EXIT.optionNumber -> consoleIO.printer.printGoodbyeMessage("We will miss you.")
            }
        } while (option != AdminOptions.EXIT.optionNumber)
    }

    private fun showAllProjects() {
        projectsUi.getAllUserProjects(session.currentUser.id)
            .onSuccess { projects ->
                projects.forEach { project ->
                    consoleIO.printer.printPlainText(project.name)
                }
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
            }
    }

    private fun createMate() {
        authenticationUi.createMate(session.currentUser.id)
            .onSuccess {
                consoleIO.printer.printCorrectOutput("Mate Created Successfully!")
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
            }
    }

    private fun createProject() {
        projectsUi.createProject()
            .onSuccess {
                consoleIO.printer.printCorrectOutput("Project Created Successfully!")
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
            }
    }
}