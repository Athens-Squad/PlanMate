package logic.use_cases.state

import logic.entities.ProgressionState
import logic.repositories.StatesRepository



class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String): Result<List<ProgressionState>> {
        return runCatching {
            stateRepository.getStates().getOrThrow().filter { it.projectId == projectId }
        }
    }
}
