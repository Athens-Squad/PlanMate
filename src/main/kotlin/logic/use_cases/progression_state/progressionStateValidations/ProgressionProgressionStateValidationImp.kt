package logic.use_cases.progression_state.progressionStateValidations

import logic.repositories.ProjectsRepository
import logic.repositories.ProgressionStateRepository


class ProgressionProgressionStateValidationImp(
    private val projectsRepository: ProjectsRepository,
    private val progressionStateRepository: ProgressionStateRepository
) : ProgressionStateValidator {

    override suspend fun validateProjectExists(projectId: String) {
        projectsRepository.getProjects().getOrNull()?.find { it.id == projectId }
            ?: throw IllegalArgumentException("Project with ID $projectId does not exist.")
    }

    override suspend fun progressionStateIsExist(progressionStateId: String) {
        val progressionStateExists = progressionStateRepository.getProgressionStates().find { it.id == progressionStateId } != null
        if (progressionStateExists) {
            throw IllegalArgumentException("State with ID '${progressionStateId}' already exists.")
        }
    }

	override suspend fun validateProgressionStateFieldsIsNotBlankOrThrow(progressionStateId: String) {
		if (progressionStateId.isBlank()) throw IllegalArgumentException("State name cannot be empty.")
	}
}