@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi

class CreateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
    suspend fun execute(progressionState: ProgressionState) {
		progressionStateValidator.validateProgressionStateFieldsNotBlank(progressionState)
	    progressionStateValidator.validateProjectExists(progressionState.projectId)
	    progressionStateValidator.validateProgressionStateNotExists(progressionState.id)
	    repository.createProgressionState(progressionState)
    }
}