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
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printError("Unexpected error: ${throwable.message}")
    }
    private val projectsScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    fun selectProject(projects: List<Project>) {
        if(projects.isEmpty()){
            consoleIO.printer.printError("No Projects Found")
            return
        }

        projectsScope.launch {
            try {
                do {
                    consoleIO.printer.printTitle("Select A Project :")
                    val inputProjectName = consoleIO.reader.readStringFromUser()

                    val project = projectsUi.getProject(getProjectId(inputProjectName, projects))

                    printProjectInfo(project)

                    if (session.currentUser.type is UserType.AdminUser) {
                        projectOptionsHandler.handleAdmin(project)
                    }

                    if (session.currentUser.type is UserType.MateUser) {
                        projectOptionsHandler.handleMate(project)
                    }

                } while (inputProjectName == "0")
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }

    }

    private fun getProjectId(inputProjectName: String, projects: List<Project>): String {
        return projects.first { it.name == inputProjectName }.id
    }

    private fun printProjectInfo(project: Project) {
        showProjectSwimlanes(project)
    }
}
