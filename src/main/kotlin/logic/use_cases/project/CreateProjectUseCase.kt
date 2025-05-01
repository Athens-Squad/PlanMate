package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.*
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import net.thechance.logic.use_cases.project.log_builder.createLog
import net.thechance.logic.use_cases.project.validations.checkIfFieldIsValid
import net.thechance.logic.use_cases.project.validations.checkIfProjectsStatesValid
import net.thechance.logic.use_cases.project.validations.checkIfProjectsTasksValid
import net.thechance.logic.use_cases.project.validations.checkIfUserAuthorized

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
