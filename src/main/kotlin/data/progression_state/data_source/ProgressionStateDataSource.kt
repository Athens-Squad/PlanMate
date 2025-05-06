package data.progression_state.data_source

import logic.entities.ProgressionState

interface ProgressionStateDataSource {
	suspend fun createProgressionState(progressionState: ProgressionState)
	suspend fun updateProgressionState(progressionState: ProgressionState)
	suspend fun deleteProgressionState(progressionStateId: String)
	suspend fun getProgressionStates(): List<ProgressionState>
	suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState>
}