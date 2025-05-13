package net.thechance.ui.presenters

import logic.entities.Project
import logic.entities.UserType
import logic.use_cases.project.GetAllProjectsByUsernameUseCase
import net.thechance.data.authentication.UserSession
import net.thechance.ui.core.Presenter
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.MateOptions

class MatePresenter(
    private val consoleIO: ConsoleIO,
    private val projectsPresenter: ProjectsPresenter,
    private val getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase,
    private val session: UserSession
) : Presenter {

    override suspend fun start() {
        while (true) {
            consoleIO.printer.printOptions(MateOptions.entries)
            when (consoleIO.reader.readNumberFromUser()) {
                MateOptions.SHOW_ALL_PROJECTS.optionNumber -> showProjects()
                MateOptions.EXIT.optionNumber -> {
                    consoleIO.printer.printText("We will miss you!", TextStyle.WELCOME)
                    break
                }

                else -> consoleIO.printer.printText("Invalid option.", TextStyle.ERROR)
            }
        }
    }

    private suspend fun showProjects() {
        val adminName = (session.currentUser.type as UserType.MateUser).adminName
        val projects = getAllProjectsByUsernameUseCase.execute(adminName)
        val selected = selectProject(projects)
        selected?.let {
            projectsPresenter.showDetails(it, isAdmin = false)

        }
    }

    private fun selectProject(projects: List<Project>): Project? {
        if (projects.isEmpty()) {
            consoleIO.printer.printText("No projects found.", TextStyle.ERROR)
            return null
        }

        consoleIO.printer.printText("Select project by name:", TextStyle.TITLE)
        projects.map { it.name }.forEach {
            consoleIO.printer.printText(it, TextStyle.OPTION)
        }

        val name = consoleIO.reader.readStringFromUser()
        return projects.find { it.name == name }
    }
}