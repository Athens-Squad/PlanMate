package ui.featuresui


import kotlinx.coroutines.*
import logic.entities.Project
import logic.entities.UserType
import logic.use_cases.project.ProjectUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.project.EditProjectOptions
import net.thechance.ui.utils.TextStyle
import ui.io.ConsoleIO

class ProjectsUi(
    private val projectUseCases: ProjectUseCases,
    private val session: UserSession,
    private val consoleIO: ConsoleIO
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printText("Unexpected error: ${throwable.message}",TextStyle.ERROR)
    }
    private val projectsScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    fun createProject() {
        consoleIO.printer.printText("Create Project.",TextStyle.TITLE)

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
                consoleIO.printer.printText("Project created successfully.",TextStyle.SUCCESS)
            } catch (exception: Exception) {
                consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
            }
        }
    }

    suspend fun editProject(project: Project) {
        consoleIO.printer.printText("Edit Project",TextStyle.TITLE)

        consoleIO.printer.printText("Select your option (1 or 2) : ",TextStyle.TITLE)

        consoleIO.printer.printOptions(EditProjectOptions.entries)

        val inputEditOption = consoleIO.reader.readNumberFromUser()

            try {
                when(inputEditOption) {
                    EditProjectOptions.NAME.optionNumber ->  editProjectName(project)
                    EditProjectOptions.DESCRIPTION.optionNumber -> editProjectDescription(project)
                    else -> throw Exception("Invalid Input!")
                }
            } catch (exception: Exception) {
                consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
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
                consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
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
        consoleIO.printer.printText(message,TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }
}