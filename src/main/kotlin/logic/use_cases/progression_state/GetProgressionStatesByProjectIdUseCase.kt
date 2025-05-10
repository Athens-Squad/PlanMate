@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetProgressionStatesByProjectIdUseCase(private val repository: ProgressionStateRepository) {
    suspend fun execute(projectId: Uuid): List<ProgressionState> {
           return repository.getProgressionStatesByProjectId(projectId)
    }
}
