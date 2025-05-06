package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository



class GetProgressionStatesByProjectIdUseCase(private val repository: ProgressionStateRepository) {
    suspend fun execute(projectId: String): List<ProgressionState> {
           return repository.getProgressionStatesByProjectId(projectId)
    }
}
