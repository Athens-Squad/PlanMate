package logic.use_cases.project

import data.projects.exceptions.ProjectsLogicExceptions.*
import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.project.log_builder.createLog
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository,
    private val projectValidator: ProjectValidator
) {
    suspend fun execute(project: Project) {
        if (
            projectValidator.checkIfValueIsBlank(project.createdBy) ||
            projectValidator.checkIfValueIsBlank(project.name)
        ) {
            throw InvalidProjectNameException()

        }


        val user = userRepository.getUserByUsername(project.createdBy)
        if (!projectValidator.checkIfUserIsAdmin(user)) {
            throw NotAuthorizedUserException()
        }

        val projects = projectRepository.getProjects()
        if (projectValidator.checkIfProjectAlreadyExistInRepository(projectId = project.id, projects)) {
            throw ProjectAlreadyExistException()
        }


        projectRepository.createProject(project)

        createLog(
            project = project,
            logMessage = "Project created successfully."
        ) {
            auditRepository.createAuditLog(it)
        }
    }
}

