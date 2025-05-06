package data.progression_state.data_source

import logic.entities.ProgressionState
import net.thechance.data.progression_state.dto.ProgressionStateDto

interface ProgressionStateDataSource {
	suspend fun createProgressionState(progressionState: ProgressionState)
	suspend fun updateProgressionState(progressionState: ProgressionState)
	suspend fun deleteProgressionState(progressionStateId: String)
	suspend fun getProgressionStates(): List<ProgressionState>
	suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState>
}