package logic.use_cases.progression_state

import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator

class CreateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
    suspend fun execute(progressionState: ProgressionState) {
	    progressionStateValidator.validateProjectExists(progressionState.projectId)
	    progressionStateValidator.progressionStateIsExist(progressionState.id)
	    progressionStateValidator.validateProgressionStateFieldsIsNotBlankOrThrow(progressionState.id)
	    repository.createProgressionState(progressionState)
    }
}