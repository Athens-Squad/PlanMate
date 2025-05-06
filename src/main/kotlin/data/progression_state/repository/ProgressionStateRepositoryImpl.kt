package data.progression_state.repository

import data.progression_state.data_source.ProgressionStateDataSource
import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import net.thechance.data.progression_state.mappers.toProgressionState
import net.thechance.data.progression_state.mappers.toProgressionStateDto


class ProgressionStateRepositoryImpl(
	private val progressionStateDataSource: ProgressionStateDataSource
): ProgressionStateRepository {
	override suspend fun createProgressionState(progressionState: ProgressionState) {
		progressionStateDataSource.createProgressionState(
			progressionState = progressionState.toProgressionStateDto()
		)
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		progressionStateDataSource.updateProgressionState(
			progressionState = progressionState.toProgressionStateDto()
		)
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		progressionStateDataSource.deleteProgressionState(
			progressionStateId = progressionStateId
		)
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		return progressionStateDataSource.getProgressionStates().map { it.toProgressionState() }
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		return progressionStateDataSource.getProgressionStatesByProjectId(projectId).map { it.toProgressionState() }
	}

}

