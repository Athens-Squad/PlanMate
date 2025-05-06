package logic.use_cases.progression_state


import logic.repositories.ProgressionStateRepository
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator

class DeleteProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
    suspend fun execute(progressionStateId: String) {
        progressionStateValidator.progressionStateIsExist(progressionStateId)
        repository.deleteProgressionState(progressionStateId)
    }
}