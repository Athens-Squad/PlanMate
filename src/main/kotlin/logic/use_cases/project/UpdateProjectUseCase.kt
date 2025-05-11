package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import java.time.LocalDateTime


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(updatedProject: Project) {

		projectValidator.validateProjectAfterCreation(
			projectId = updatedProject.id,
			username = updatedProject.createdBy
		)

        projectRepository.updateProject(updatedProject)

        createAuditLogUseCase.execute(
            AuditLog(
                entityType = EntityType.PROJECT,
                entityId = updatedProject.id,
                description = "Project updated successfully.",
                userName = updatedProject.createdBy,
                createdAt = LocalDateTime.now(),
            )
        )
    }
}