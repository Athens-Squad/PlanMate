package logic.repositories

import logic.entities.ProgressionState

interface ProgressionStateRepository {
	suspend fun createProgressionState(progressionState: ProgressionState)
	suspend fun updateProgressionState(progressionState: ProgressionState)
	suspend fun deleteProgressionState(progressionStateId: String)
	suspend fun getProgressionStates(): List<ProgressionState>
	suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState>
}