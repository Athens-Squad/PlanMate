@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(projectId: Uuid, username: String) {
        username.apply {
            checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
            checkIfUserAuthorized(username) { userRepository.getUserByUsername(it) }
                .takeIf { it } ?: throw NotAuthorizedUserException()
        }

        projectId.toString().checkIfFieldIsValid().takeIf { it } ?: throw NoProjectFoundException()
        val project = checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
            ?: throw NoProjectFoundException()

        checkIfUserIsProjectOwner(username, project.createdByUserName).takeIf { it }
            ?: throw NotAuthorizedUserException()

        projectRepository.deleteProject(projectId)

        createAuditLogUseCase.execute(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = project.id,
                description = "Project deleted successfully.",
                userName = project.createdByUserName,
                createdAt = LocalDateTime.now(),
            )
        )
    }
}
