@file:OptIn(ExperimentalUuidApi::class)

package logic.repositories

import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProgressionStateRepository {
	suspend fun createProgressionState(progressionState: ProgressionState)
	suspend fun updateProgressionState(progressionState: ProgressionState)
	suspend fun deleteProgressionState(progressionStateId: Uuid)
	suspend fun getProgressionStates(): List<ProgressionState>
	suspend fun getProgressionStatesByProjectId(projectId: Uuid): List<ProgressionState>
}