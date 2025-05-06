package net.thechance.ui.handlers

import kotlinx.coroutines.*
import ui.io.ConsoleIO
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.AdminOptions
import net.thechance.ui.utils.ProjectSelector
import ui.featuresui.*

class AdminOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val projectSelector: ProjectSelector,
    private val authenticationUi: AuthenticationUi,
    private val session: UserSession
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printError("Unexpected error: ${throwable.message}")
    }
    private val projectsScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


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
        projectsScope.launch {
            try {
                val projects = projectsUi.getAllUserProjects(session.currentUser.name)

                projects.forEach { project ->
                    consoleIO.printer.printPlainText(project.name)
                }

                projectSelector.selectProject(projects)
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }

    }

    private fun createMate() {
        authenticationUi.createMate(session.currentUser.name)
        consoleIO.printer.printCorrectOutput("Mate Created Successfully!")

    }

    private fun createProject() {
        projectsUi.createProject()
        consoleIO.printer.printCorrectOutput("Project Created Successfully!")
    }
}