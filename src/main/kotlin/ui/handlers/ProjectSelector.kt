package net.thechance.ui.handlers

import logic.entities.Project
import ui.featuresui.ProjectsUi
import ui.io.ConsoleIO

class ProjectSelector(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val projectOptionsHandler: ProjectOptionsHandler,
    private val projectSwimlaneUiPrinter: ProjectSwimlaneUiPrinter
) {

    fun selectProject(projects: List<Project>) {
        do {
            consoleIO.printer.printTitle("Select A Project :")
            val inputProjectName = consoleIO.reader.readStringFromUser()

            getProjectId(inputProjectName, projects)
                .onSuccess { projectId ->
                    projectsUi.getProject(projectId)
                        .onSuccess { project ->
                            printProjectInfo(project)
                            projectOptionsHandler.handle(project)
                        }
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

    private fun printProjectInfo(project: Project) {
        consoleIO.printer.printTitle("Project: ${project.name}")
        consoleIO.printer.printPlainText("Description: ${project.description}")
        consoleIO.printer.printDivider()

        projectSwimlaneUiPrinter.printSwimlanes(project)
    }
}
