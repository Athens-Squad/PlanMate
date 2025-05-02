package net.thechance.ui.handlers

import logic.entities.Project
import ui.featuresui.ProjectsUi
import ui.io.ConsoleIO

class ProjectSelector(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi
) {

    fun selectProject(projects: List<Project>, onProjectSelected: (Project) -> Unit) {
        do {
            consoleIO.printer.printTitle("Select A Project :")
            val inputProjectName = consoleIO.reader.readStringFromUser()

            val result = getProjectId(inputProjectName, projects)
            result
                .onSuccess { projectId ->
                    projectsUi.getProject(projectId)
                        .onSuccess(onProjectSelected)
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }
                }
                .onFailure {
                    consoleIO.printer.printError(it.message.toString())
                }

            consoleIO.printer.printOption("0 : Back")

        } while (inputProjectName == "0")
    }

    private fun getProjectId(inputProjectName: String, projects: List<Project>): Result<String> {
        return runCatching {
            projects.first { it.name == inputProjectName }.id
        }
    }
}
