package logic.use_cases.project

import logic.entities.Project
import logic.repositories.*
import logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import logic.use_cases.project.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectsStatesValid
import logic.use_cases.project.projectValidations.checkIfProjectsTasksValid
import logic.use_cases.project.projectValidations.checkIfUserAuthorized

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val statesRepository: StatesRepository,
    private val tasksRepository: TasksRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(project: Project): Result<Unit> {
        return runCatching {

            project.apply {
                createdBy.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
                name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

                checkIfUserAuthorized(project.createdBy) { userRepository.getUserByUsername(it) }
                    .takeIf { it } ?: throw NotAuthorizedUserException()
                checkIfProjectsStatesValid(project.id) { statesRepository.getStates() }
                    .takeIf { it } ?: throw NoStatesFoundForProjectException()
                checkIfProjectsTasksValid(project.id) { tasksRepository.getTasksByProjectId(it) }
                    .takeIf { it } ?: throw NoTasksFoundForProjectException()
            }

            projectRepository.createProject(project)
                .onSuccess {
                    createLog(
                        project = project,
                        logMessage = "Project created successfully."
                    ) {
                        auditRepository.createAuditLog(it).getOrThrow()
                    }
                }
        }
    }
}
