@file:OptIn(ExperimentalUuidApi::class)

package data.progression_state.data_source

import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProgressionStateDataSource {
	suspend fun createProgressionState(progressionState: ProgressionState)
	suspend fun updateProgressionState(progressionState: ProgressionState)
	suspend fun deleteProgressionState(progressionStateId: Uuid)
	suspend fun getProgressionStates(): List<ProgressionState>
	suspend fun getProgressionStatesByProjectId(projectId: Uuid): List<ProgressionState>
}