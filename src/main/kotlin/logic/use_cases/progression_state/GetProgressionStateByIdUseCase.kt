package logic.use_cases.progression_state

import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import net.thechance.data.progression_state.exceptions.ProgressionStateNotFoundException

class GetProgressionStateByIdUseCase(
	private val repository: ProgressionStateRepository,
) {
    suspend fun execute(progressionStateId: String): ProgressionState {
           return repository.getProgressionStates().find { it.id == progressionStateId }
               ?: throw ProgressionStateNotFoundException()
    }
}