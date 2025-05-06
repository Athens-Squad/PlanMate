package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.UseCaseValidator


class UpdateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: UseCaseValidator<ProgressionState>
) {
	suspend fun execute(updatedProgressionState: ProgressionState) {
		progressionStateValidator
			.validateAfterCreation(updatedProgressionState.id)
			?.let { throw it }

		repository.updateProgressionState(updatedProgressionState)
	}
}
