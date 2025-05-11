package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator

class GetAllProjectsByUsernameUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
) {
    suspend fun execute(username: String): List<Project> {
        return projectRepository.getProjects()
	        .filter { projectValidator.validateProjectAfterCreation(projectId = it.id, username = username) }
			.filter { it.createdBy == username }
    }
}