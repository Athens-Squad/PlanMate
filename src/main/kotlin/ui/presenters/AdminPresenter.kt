@file:OptIn(kotlin.uuid.ExperimentalUuidApi::class)

package net.thechance.ui.presenters

import logic.entities.Project
import logic.entities.User
import logic.entities.UserType
import logic.use_cases.authentication.RegisterAsMateUseCase
import logic.use_cases.project.GetAllProjectsByUsernameUseCase
import net.thechance.data.authentication.UserSession
import net.thechance.ui.core.Presenter
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.featuresui.ProjectsUi
import net.thechance.ui.options.AdminOptions

class AdminPresenter(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val projectsPresenter: ProjectsPresenter,
    private val getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase,
    private val registerAsMateUseCase: RegisterAsMateUseCase,
    private val session: UserSession
) : Presenter {

    override suspend fun start() {
        while (true) {
            try {
                consoleIO.printer.printOptions(AdminOptions.entries)
                when (consoleIO.reader.readNumberFromUser()) {
                    AdminOptions.SHOW_ALL_PROJECTS.optionNumber -> showProjects()
                    AdminOptions.CREATE_PROJECT.optionNumber -> projectsUi.createProject()
                    AdminOptions.CREATE_MATE.optionNumber -> createMate()
                    AdminOptions.EXIT.optionNumber -> {
                        consoleIO.printer.printText("We will miss you!", TextStyle.WELCOME)
                        break
                    }

                    else -> consoleIO.printer.printText("Invalid option.", TextStyle.ERROR)
                }
            } catch (exception: Exception) {
                consoleIO.printer.printText("Error : " + exception.message.toString(), TextStyle.ERROR)
            }
        }

    }

    private suspend fun showProjects() {
        val projects = getAllProjectsByUsernameUseCase.execute(session.currentUser.name)
        val selected = selectProject(projects)
        selected?.let {
            projectsPresenter.showDetails(it, isAdmin = true)
        }
    }

    private fun selectProject(projects: List<Project>): Project? {
        if (projects.isEmpty()) {
            consoleIO.printer.printText("No projects found.", TextStyle.ERROR)
            return null
        }

        projects.map { it.name }.forEach {
            consoleIO.printer.printText(it, TextStyle.OPTION)
        }
        consoleIO.printer.printText("Select project by name:", TextStyle.TITLE)

        val name = consoleIO.reader.readStringFromUser()
        return projects.find { it.name == name }
    }

    private suspend fun createMate() {
        consoleIO.printer.printText("Create Mate Account, Please Enter Mate's Info : ", TextStyle.TITLE)
        val userName = receiveUserInfo("Enter Mate's Username : ")
        val password = receiveUserInfo("Enter Mate's Password : ")

        try {
            registerAsMateUseCase.execute(
                mateUser = User(
                    name = userName,
                    type = UserType.MateUser(session.currentUser.name)
                ),
                password = password
            )
            consoleIO.printer.printText("Mate Created Successfully!", TextStyle.SUCCESS)
        } catch (exception: Exception) {
            consoleIO.printer.printText("Error : ${exception.message}", TextStyle.ERROR)
        }
    }

    private fun receiveUserInfo(message: String): String {
        consoleIO.printer.printText(message)
        return consoleIO.reader.readStringFromUser()
    }
}