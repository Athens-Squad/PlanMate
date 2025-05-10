@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectAlreadyExistInRepository
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NotAuthorizedUserException
import net.thechance.logic.exceptions.ProjectAlreadyExistException
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(project: Project) {
        project.apply {
            createdByUserName.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
            name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

            checkIfUserAuthorized(createdByUserName) { userRepository.getUserByUsername(createdByUserName) }
                .takeIf { it } ?: throw NotAuthorizedUserException()

            checkIfProjectAlreadyExistInRepository(id) { projectRepository.getProjects() }
                .takeIf { it } ?: throw ProjectAlreadyExistException()
        }

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
