@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state


import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteProgressionStateUseCase(
	private val repository: ProgressionStateRepository,
	private val progressionStateValidator: ProgressionStateValidator
) {
	suspend fun execute(progressionStateId: Uuid) {
		progressionStateValidator
			.validateAfterCreation(progressionStateId)
			?.let { throw it }

		repository.deleteProgressionState(progressionStateId)
	}
}