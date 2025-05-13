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

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(project: Project) {
        projectValidator.validateProjectFieldsNotBlank(project)
        projectValidator.validateUserIsAuthorized(project.createdByUserName)
        projectValidator.validateProjectNotExists(project.id)

        projectRepository.createProject(project)

        createAuditLogUseCase.execute(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = project.id,
                description = "Project created successfully.",
                userName = project.createdByUserName,
                createdAt = LocalDateTime.now(),
            )
        )
    }
}
