@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.validators.projectValidations.ProjectValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetProjectByIdUseCase(
	private val projectRepository: ProjectsRepository,
	private val projectValidator: ProjectValidator
) {
	suspend fun execute(projectId: Uuid): Project {
		projectValidator.validateProjectAlreadyExists(projectId)
		return projectRepository.getProjects().first { it.id == projectId }
	}
}