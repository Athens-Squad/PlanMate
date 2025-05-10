package net.thechance.ui.utils

import kotlinx.coroutines.*
import logic.entities.Project
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.handlers.ProjectOptionsHandler
import ui.featuresui.ProjectsUi
import ui.io.ConsoleIO

class ProjectSelector(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val projectOptionsHandler: ProjectOptionsHandler,
    private val showProjectSwimlanes: ShowProjectSwimlanes,
    private val session: UserSession
) {
    suspend fun selectProject(projects: List<Project>) {
        if(projects.isEmpty()){
            consoleIO.printer.printText("No Projects Found",TextStyle.ERROR)
            return
        }

        try {
            do {
                consoleIO.printer.printText("Select A Project :",TextStyle.TITLE)
                val inputProjectName = consoleIO.reader.readStringFromUser()

                val project = projectsUi.getProject(getProjectId(inputProjectName, projects))

                handleProject(project)

            } while (inputProjectName == "0")
        } catch (exception: Exception) {
            consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
        }

    }
    private suspend fun handleProject(project: Project) {
        showProjectSwimlanes(project)

        if (session.currentUser.type is UserType.AdminUser) {
            projectOptionsHandler.handleAdmin(project)
        }

        if (session.currentUser.type is UserType.MateUser) {
            projectOptionsHandler.handleMate(project)
        }
    }

    private fun getProjectId(inputProjectName: String, projects: List<Project>): String {
        return projects.first { it.name == inputProjectName }.id
    }

}
