package data.progression_state.data_source.database

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.progression_state.data_source.ProgressionStateDataSource
import logic.entities.ProgressionState

class ProgressionStateDatabaseDataSource(
	progressionStatesDocument: MongoCollection<MongoDatabase>
): ProgressionStateDataSource {
	override suspend fun createProgressionState(progressionState: ProgressionState) {
		TODO("Not yet implemented")
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		TODO("Not yet implemented")
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		TODO("Not yet implemented")
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		TODO("Not yet implemented")
	}

}