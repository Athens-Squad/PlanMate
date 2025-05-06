package ui.featuresui


import kotlinx.coroutines.*
import logic.entities.Project
import logic.entities.UserType
import logic.use_cases.project.ProjectUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.project.EditProjectOptions
import ui.io.ConsoleIO

class ProjectsUi(
    private val projectUseCases: ProjectUseCases,
    private val session: UserSession,
    private val consoleIO: ConsoleIO
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printError("Unexpected error: ${throwable.message}")
    }
    private val projectsScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    fun createProject() {
        consoleIO.printer.printTitle("Create Project.")

        val projectName = receiveStringInput("Enter Project Name : ")
        val projectDescription = receiveStringInput("Enter Project Description : ")

        projectsScope.launch {
            try {
                projectUseCases.createProjectUseCase.execute(
                    Project(
                        name = projectName,
                        description = projectDescription,
                        createdBy = session.currentUser.name
                    )
                )
                consoleIO.printer.printCorrectOutput("Project created successfully.")
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }
    }

    fun editProject(project: Project) {
        consoleIO.printer.printTitle("Edit Project")

        consoleIO.printer.printTitle("Select your option (1 or 2) : ")

        consoleIO.printer.printOptions(EditProjectOptions.entries)

        val inputEditOption = consoleIO.reader.readNumberFromUser()

        projectsScope.launch {
            try {
                when(inputEditOption) {
                    EditProjectOptions.NAME.optionNumber ->  editProjectName(project)
                    EditProjectOptions.DESCRIPTION.optionNumber -> editProjectDescription(project)
                    else -> throw Exception("Invalid Input!")
                }
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }

    }

    private suspend fun editProjectDescription(project: Project) {
        val projectDescription = receiveStringInput("Enter New Project Description : ")

        projectUseCases.updateProjectUseCase
            .execute(project.copy(description = projectDescription))
    }

    private suspend fun editProjectName(project: Project) {
        val projectName = receiveStringInput("Enter New Project Name : ")

        projectUseCases.updateProjectUseCase.execute(project.copy(name = projectName))
    }

    fun deleteProject(projectId: String) {
        projectsScope.launch {
            try {
                projectUseCases.deleteProjectUseCase
                    .execute(projectId, session.currentUser.name)
            } catch (exception: Exception) {
                consoleIO.printer.printError("Error : ${exception.message}")
            }
        }
    }



    suspend fun getProject(projectId: String): Project {
        return projectUseCases.getProjectByIdUseCase.execute(projectId)
    }

    suspend fun getAllUserProjects(userName: String): List<Project> {
        return projectUseCases.getAllProjectsByUsernameUseCase.execute(userName)
    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }
}