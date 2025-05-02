package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.ProgressionState

class GetStateByIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(stateId: String): Result<ProgressionState> {
       return runCatching {
           stateRepository.getStates().getOrThrow().find { it.id == stateId }
               ?: throw IllegalArgumentException("Project with ID $stateId does not exist.")
       }
    }
}