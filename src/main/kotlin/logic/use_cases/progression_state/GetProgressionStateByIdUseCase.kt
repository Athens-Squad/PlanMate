package logic.use_cases.progression_state

import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import logic.exceptions.ProgressionStateNotFoundException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetProgressionStateByIdUseCase(
	private val repository: ProgressionStateRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun execute(progressionStateId: Uuid): ProgressionState {
           return repository.getProgressionStates().find { it.id == progressionStateId }
               ?: throw ProgressionStateNotFoundException()
    }
}