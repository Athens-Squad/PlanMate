package logic.use_cases.project

import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.project.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    suspend fun execute(updatedProject: Project) {
        updatedProject.apply {
            createdBy.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()

            name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

            checkIfUserAuthorized(createdBy) { userRepository.getUserByUsername(it) }
                .takeIf { it } ?: throw NotAuthorizedUserException()

            val project = checkIfProjectExistInRepositoryAndReturn(updatedProject.id) { projectRepository.getProjects() }
                ?: throw NoProjectFoundException()

            checkIfUserIsProjectOwner(project.createdBy, updatedProject.createdBy).takeIf { it }
                ?: throw NotAuthorizedUserException()
        }

        projectRepository.updateProject(updatedProject)

        createLog(
            project = updatedProject,
            logMessage = "Project updated successfully."
        ) {
            auditRepository.createAuditLog(it)
        }
    }
}