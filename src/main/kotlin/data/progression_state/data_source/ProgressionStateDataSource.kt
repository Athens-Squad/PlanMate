package data.progression_state.data_source

import net.thechance.data.progression_state.dto.ProgressionStateDto

interface ProgressionStateDataSource {
	suspend fun createProgressionState(progressionState: ProgressionStateDto)
	suspend fun updateProgressionState(progressionState: ProgressionStateDto)
	suspend fun deleteProgressionState(progressionStateId: String)
	suspend fun getProgressionStates(): List<ProgressionStateDto>
	suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionStateDto>
}