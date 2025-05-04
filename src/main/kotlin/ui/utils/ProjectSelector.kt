package net.thechance.ui.utils

import logic.entities.Project
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.handlers.ProjectOptionsHandler
import ui.featuresui.ProjectsUi
import ui.featuresui.TasksUi
import ui.io.ConsoleIO

class ProjectSelector(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val tasksUi: TasksUi,
    private val projectOptionsHandler: ProjectOptionsHandler,
    private val showProjectSwimlanes: ShowProjectSwimlanes,
    private val session: UserSession
) {

    fun selectProject(projects: List<Project>) {
        if(projects.isEmpty()){
            consoleIO.printer.printError("No projects")
            return
        }
        do {
            consoleIO.printer.printTitle("Select A Project :")
            val inputProjectName = consoleIO.reader.readStringFromUser()

            getProjectId(inputProjectName, projects)
                .onSuccess { projectId ->
                    projectsUi.getProject(projectId)
                        .onSuccess { project ->
                            printProjectInfo(project)

                            if (session.currentUser.type is UserType.AdminUser) {
                                projectOptionsHandler.handleAdmin(project)
                            }

                            if (session.currentUser.type is UserType.MateUser) {
                                projectOptionsHandler.handleMate(project)
                            }
                        }
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }
                }
                .onFailure {
                    consoleIO.printer.printError(it.message.toString())
                }

        } while (inputProjectName == "0")
    }

    private fun getProjectId(inputProjectName: String, projects: List<Project>): Result<String> {
        return runCatching {
            projects.first { it.name == inputProjectName }.id
        }
    }

    private fun printProjectInfo(project: Project) {
        showProjectSwimlanes(project)
    }
}
