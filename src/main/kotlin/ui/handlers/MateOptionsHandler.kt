package net.thechance.ui.handlers

import logic.entities.UserType
import ui.io.ConsoleIO
import net.thechance.data.authentication.UserSession
import net.thechance.logic.use_cases.user.GetUserByUsernameUseCase
import net.thechance.ui.options.MateOptions
import ui.featuresui.*

class MateOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val getUserByUsernameUseCase: GetUserByUsernameUseCase,
    private val session: UserSession
) {
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
        val adminId = (session.currentUser.type as UserType.MateUser).adminId

        getUserByUsernameUseCase.execute(adminId)
            .onSuccess { admin ->
                projectsUi.getAllUserProjects(admin.name)
                    .onSuccess {
                        it.forEach { project ->
                            consoleIO.printer.printPlainText(project.name)
                        }
                    }
                    .onFailure {
                        consoleIO.printer.printError(it.message.toString())
                    }
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
            }
    }
}