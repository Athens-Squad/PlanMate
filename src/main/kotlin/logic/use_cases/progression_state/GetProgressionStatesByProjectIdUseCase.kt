@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.validators.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetProgressionStatesByProjectIdUseCase(
    private val progressionStateValidator: ProgressionStateValidator,
    private val repository: ProgressionStateRepository
) {
    suspend fun execute(projectId: Uuid): List<ProgressionState> {
		progressionStateValidator.validateProjectExists(projectId)
        return repository.getProgressionStatesByProjectId(projectId)
    }
}
