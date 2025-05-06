package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator


class UpdateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
    suspend fun execute(updatedProgressionState: ProgressionState) {
            progressionStateValidator.progressionStateIsExist(updatedProgressionState.id)
            progressionStateValidator.validateProjectExists(updatedProgressionState.id)
            repository.updateProgressionState(updatedProgressionState)
    }
}
