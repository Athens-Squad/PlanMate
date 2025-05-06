package net.thechance.ui.handlers

import kotlinx.coroutines.*
import logic.entities.UserType
import ui.io.ConsoleIO
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.MateOptions
import net.thechance.ui.utils.ProjectSelector
import ui.featuresui.*

class MateOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val projectSelector: ProjectSelector,
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

            consoleIO.printer.printOptions(MateOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                MateOptions.SHOW_ALL_PROJECTS.optionNumber -> showAllProjects()
                MateOptions.EXIT.optionNumber -> consoleIO.printer.printGoodbyeMessage("We will miss you.")
            }
        } while (option != MateOptions.EXIT.optionNumber)
    }

    private fun showAllProjects() {
        val adminName = (session.currentUser.type as UserType.MateUser).adminName
        projectsScope.launch {
            try {
                val projects = projectsUi.getAllUserProjects(adminName)

                projects.forEach { project ->
                    consoleIO.printer.printPlainText(project.name)
                }

                projectSelector.selectProject(projects)
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }

    }
}