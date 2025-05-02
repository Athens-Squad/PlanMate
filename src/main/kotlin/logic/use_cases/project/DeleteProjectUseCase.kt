package logic.use_cases.project

import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import data.projects.exceptions.ProjectsLogicExceptions.*
import logic.use_cases.project.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(username: String, projectId: String): Result<Unit> {
        return runCatching {

            username.apply {
                checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
                checkIfUserAuthorized(username) { userRepository.getUserByUsername(it) }
                    .takeIf { it } ?: throw NotAuthorizedUserException()
            }

            projectId.checkIfFieldIsValid().takeIf { it } ?: throw NoProjectFoundException()
            val project = checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
                ?: throw NoProjectFoundException()

            checkIfUserIsProjectOwner(username, project.createdBy).takeIf { it }
                ?: throw NotAuthorizedUserException()

            projectRepository.deleteProject(projectId)
                .onSuccess {
                    createLog(
                        project = project,
                        logMessage = "Project deleted successfully."
                    ) {
                        auditRepository.createAuditLog(it).getOrThrow()
                    }
                }
        }
    }
}
