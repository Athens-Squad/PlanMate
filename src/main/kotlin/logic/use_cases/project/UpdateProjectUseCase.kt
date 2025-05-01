package logic.use_cases.project

import logic.entities.Project
import logic.repositories.*
import logic.use_cases.project.projectValidations.*
import logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import logic.use_cases.project.log_builder.createLog


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val statesRepository: StatesRepository,
    private val tasksRepository: TasksRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(updatedProject: Project): Result<Unit> {
        return runCatching{

            updatedProject.apply {
                createdBy.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
                name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

                checkIfUserAuthorized(createdBy) { userRepository.getUserByUsername(it) }
                    .takeIf { it } ?: throw NotAuthorizedUserException()

                checkIfProjectsStatesValid(id) { statesRepository.getStates() }
                    .takeIf { it } ?: throw NoStatesFoundForProjectException()

                checkIfProjectsTasksValid(id) { tasksRepository.getTasksByProjectId(it) }
                    .takeIf { it } ?: throw NoTasksFoundForProjectException()

                val project = checkIfProjectExistInRepositoryAndReturn(updatedProject.id) { projectRepository.getProjects() }
                    ?: throw NoProjectFoundException()

                checkIfUserIsProjectOwner(project.createdBy, updatedProject.createdBy).takeIf { it }
                    ?: throw NotAuthorizedUserException()
            }

            projectRepository.updateProject(updatedProject)
                .onSuccess {
                    createLog(
                        project = updatedProject,
                        logMessage = "Project updated successfully."
                    ) {
                        auditRepository.createAuditLog(it).getOrThrow()
                    }
                }
            }
    }
}