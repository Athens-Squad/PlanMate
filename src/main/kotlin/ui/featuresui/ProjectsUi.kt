@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.featuresui

import logic.entities.Project
import logic.use_cases.project.ProjectUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.project.EditProjectOptions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectsUi(
    private val projectUseCases: ProjectUseCases,
    private val session: UserSession,
    private val consoleIO: ConsoleIO
) {

    suspend fun createProject() {
        consoleIO.printer.printText("Create Project.", TextStyle.TITLE)

        val projectName = receiveStringInput("Enter Project Name : ")
        val projectDescription = receiveStringInput("Enter Project Description : ")

        try {
            projectUseCases.createProjectUseCase.execute(
                Project(
                    name = projectName,
                    description = projectDescription,
                    createdByUserName = session.currentUser.name
                )
            )
            consoleIO.printer.printText("Project created successfully.", TextStyle.SUCCESS)
        } catch (exception: Exception) {
            consoleIO.printer.printText("Error : ${exception.message}", TextStyle.ERROR)
        }
    }

    suspend fun editProject(project: Project) {
        consoleIO.printer.printText("Edit Project", TextStyle.TITLE)
        consoleIO.printer.printText("Select your option (1 or 2) : ", TextStyle.TITLE)
        consoleIO.printer.printOptions(EditProjectOptions.entries)

        try {
            val inputEditOption = consoleIO.reader.readNumberFromUser()
            when (inputEditOption) {
                EditProjectOptions.NAME.optionNumber -> editProjectName(project)
                EditProjectOptions.DESCRIPTION.optionNumber -> editProjectDescription(project)
                else -> throw Exception("Invalid Input!")
            }
        } catch (exception: Exception) {
            consoleIO.printer.printText("Error : ${exception.message}", TextStyle.ERROR)
        }

    }

    private suspend fun editProjectName(project: Project) {
        val projectName = receiveStringInput("Enter New Project Name : ")

        projectUseCases.updateProjectUseCase.execute(project.copy(name = projectName))
    }

    private suspend fun editProjectDescription(project: Project) {
        val projectDescription = receiveStringInput("Enter New Project Description : ")

        projectUseCases.updateProjectUseCase
            .execute(project.copy(description = projectDescription))
    }

    suspend fun deleteProject(projectId: Uuid) {
            try {
                projectUseCases.deleteProjectUseCase
                    .execute(projectId, session.currentUser.name)
            } catch (exception: Exception) {
                consoleIO.printer.printText("Error : ${exception.message}", TextStyle.ERROR)
            }

    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message, TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }
}