@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.ProjectsRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.validators.projectValidations.ProjectValidator
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(projectId: Uuid, username: String) {
		projectValidator.validateUserIsAuthorized(username)
	    projectValidator.validateProjectAlreadyExists(projectId)
	    projectValidator.validateUserIsTheProjectOwner(projectId, username)

	    projectRepository.deleteProject(projectId)

        createAuditLogUseCase.execute(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = projectId,
                description = "Project deleted successfully.",
                userName = username,
                createdAt = LocalDateTime.now(),
            )
        )
    }
}
