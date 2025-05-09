package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.NoProjectFoundException

class GetProjectByIdUseCase(
	private val projectRepository: ProjectsRepository,

	) {
	suspend fun execute(projectId: String): Project {
		projectId.apply {
			checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()
		}

		return checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
			?: throw NoProjectFoundException()
	}
}