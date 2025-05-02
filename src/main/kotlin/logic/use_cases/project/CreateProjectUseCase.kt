package logic.use_cases.project

import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import data.projects.exceptions.ProjectsLogicExceptions.*
import logic.use_cases.project.log_builder.createLog
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectAlreadyExistInRepository
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import logic.use_cases.project.projectValidations.checkIfUserAuthorized

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(project: Project): Result<Unit> {
        return runCatching {

            project.apply {
                createdBy.checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
                name.checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()

                checkIfUserAuthorized(createdBy) { userRepository.getUserByUsername(createdBy) }
                    .takeIf { it } ?: throw NotAuthorizedUserException()

                checkIfProjectAlreadyExistInRepository(id) { projectRepository.getProjects() }
                    .takeIf { it } ?: throw ProjectAlreadyExistException()
            }

            projectRepository.createProject(project)
                .onSuccess {
                    createLog(
                        project = project,
                        logMessage = "Project created successfully."
                    ) {
                        auditRepository.createAuditLog(it).getOrThrow()
                    }
                }
        }
    }
}
