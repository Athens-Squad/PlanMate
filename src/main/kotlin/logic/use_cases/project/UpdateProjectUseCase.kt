@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
) {
    suspend fun execute(updatedProject: Project) {
        updatedProject.apply {
            createdByUserName.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()

            name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

            checkIfUserAuthorized(createdByUserName) { userRepository.getUserByUsername(it) }
                .takeIf { it } ?: throw NotAuthorizedUserException()

            val project =
                checkIfProjectExistInRepositoryAndReturn(updatedProject.id) { projectRepository.getProjects() }
                    ?: throw NoProjectFoundException()

            checkIfUserIsProjectOwner(project.createdByUserName, updatedProject.createdByUserName).takeIf { it }
                ?: throw NotAuthorizedUserException()
        }

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