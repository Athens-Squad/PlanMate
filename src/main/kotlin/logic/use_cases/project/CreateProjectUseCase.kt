package logic.use_cases.project

import logic.entities.EntityType
import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.use_cases.audit_log.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectAlreadyExistInRepository
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NotAuthorizedUserException
import net.thechance.logic.exceptions.ProjectAlreadyExistException

class CreateProjectUseCase(
	private val projectRepository: ProjectsRepository,
	private val userRepository: UserRepository,
	private val auditRepository: AuditRepository
) {
	suspend fun execute(project: Project) {
		project.apply {
			createdBy.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
			name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

			checkIfUserAuthorized(createdBy) { userRepository.getUserByUsername(createdBy) }
				.takeIf { it } ?: throw NotAuthorizedUserException()

			checkIfProjectAlreadyExistInRepository(id) { projectRepository.getProjects() }
				.takeIf { it } ?: throw ProjectAlreadyExistException()
		}

		projectRepository.createProject(project)

		createLog(
            logMessage = "Project created successfully.",
            entityType = EntityType.PROJECT,
            entityId = project.id,
            userName = project.name
        ) {
			auditRepository.createAuditLog(it)
		}
	}
}

