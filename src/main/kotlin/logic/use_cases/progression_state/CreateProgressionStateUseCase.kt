package logic.use_cases.progression_state

import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.UseCaseValidator

class CreateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: UseCaseValidator<ProgressionState>
) {
    suspend fun execute(progressionState: ProgressionState) {
		progressionStateValidator
			.validateBeforeCreation(progressionState)
			?.let { throw it }

	    repository.createProgressionState(progressionState)
    }
}