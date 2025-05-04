package ui.featuresui

import logic.entities.Project
import logic.use_cases.project.ProjectUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.project.EditProjectOptions
import ui.io.ConsoleIO

class ProjectsUi(
    private val projectUseCases: ProjectUseCases,
    private val session: UserSession,
    private val consoleIO: ConsoleIO
) {
    fun createProject(): Result<Unit> {
        consoleIO.printer.printTitle("Create Project.")

        val projectName = receiveStringInput("Enter Project Name : ")
        val projectDescription = receiveStringInput("Enter Project Description : ")

        return projectUseCases.createProjectUseCase.execute(
            Project(
            name = projectName,
            description = projectDescription,
            createdBy = session.currentUser.name
            )
        )
    }

    fun editProject(project: Project): Result<Unit> {
        consoleIO.printer.printTitle("Edit Project")

        consoleIO.printer.printTitle("Select your option (1 or 2) : ")

        consoleIO.printer.printOptions(EditProjectOptions.entries)

        val inputEditOption = consoleIO.reader.readNumberFromUser()

        return when(inputEditOption) {
            EditProjectOptions.NAME.optionNumber ->  editProjectName(project)
            EditProjectOptions.DESCRIPTION.optionNumber -> editProjectDescription(project)
            else -> Result.failure(Exception("Invalid Input!"))
        }
    }

    private fun editProjectDescription(project: Project): Result<Unit> {
        val projectDescription = receiveStringInput("Enter New Project Description : ")

        return projectUseCases
            .updateProjectUseCase
            .execute(project.copy(description = projectDescription))
    }

    private fun editProjectName(project: Project): Result<Unit> {
        val projectName = receiveStringInput("Enter New Project Name : ")

        return projectUseCases.updateProjectUseCase.execute(project.copy(name = projectName))

    }

    fun deleteProject(projectId: String): Result<Unit> {
        return projectUseCases
            .deleteProjectUseCase
            .execute(projectId, session.currentUser.name)
    }

    fun getProject(projectId: String): Result<Project> {
        return projectUseCases.getProjectByIdUseCase.execute(projectId)
    }

    fun getAllUserProjects(userName: String): Result<List<Project>> {
        return projectUseCases.getAllProjectsByUsernameUseCase.execute(userName)
    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }
}