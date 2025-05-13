@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.validators.projectValidations.ProjectValidator
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(updatedProject: Project) {
	    projectValidator.validateProjectFieldsNotBlank(updatedProject)
	    projectValidator.validateUserIsAuthorized(updatedProject.createdByUserName)
	    projectValidator.validateProjectAlreadyExists(updatedProject.id)
	    projectValidator.validateUserIsTheProjectOwner(updatedProject.id, updatedProject.createdByUserName)

        projectRepository.updateProject(updatedProject)

        createAuditLogUseCase.execute(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = updatedProject.id,
                description = "Project updated successfully.",
                userName = updatedProject.createdByUserName,
                createdAt = LocalDateTime.now(),
            )
        )
    }
}