@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi


class UpdateProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
	suspend fun execute(updatedProgressionState: ProgressionState) {
		progressionStateValidator.validateProgressionStateFieldsNotBlank(updatedProgressionState)
		progressionStateValidator.validateProjectExists(updatedProgressionState.projectId)
		progressionStateValidator.validateProgressionStateAlreadyExists(updatedProgressionState.id)
		repository.updateProgressionState(updatedProgressionState)
	}
}
