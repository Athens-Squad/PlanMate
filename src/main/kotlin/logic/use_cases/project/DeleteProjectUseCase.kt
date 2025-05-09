package logic.use_cases.project

import logic.entities.EntityType
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.use_cases.audit_log.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(projectId: String, username: String) {
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
        createLog(
            entityType = EntityType.PROJECT,
            entityId = project.id,
            userName = project.createdBy,
            logMessage = "Project deleted successfully."
        ) {
            createAuditLogUseCase.execute(it)
        }
    }
}
