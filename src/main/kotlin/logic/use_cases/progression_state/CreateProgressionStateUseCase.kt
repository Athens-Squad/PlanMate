package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator

class CreateProgressionStateUseCase(
    private val repository: ProgressionStateRepository,
    private val progressionStateValidator: ProgressionStateValidator
) {
    suspend fun execute(progressionState: ProgressionState) {
        progressionStateValidator
            .validateBeforeCreation(progressionState)
            ?.let { throw it }

        repository.createProgressionState(progressionState)
    }
}