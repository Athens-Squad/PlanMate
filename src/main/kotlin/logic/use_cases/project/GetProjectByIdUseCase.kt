package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator

class GetProjectByIdUseCase(private val projectRepository: ProjectsRepository,
	private val projectValidator: ProjectValidator
) {
	suspend fun execute(projectId: String): Project {
		return projectRepository.getProjects().first {
			projectValidator.validateProjectAfterCreation(projectId = projectId, username = it.createdBy)
		}
	}
}