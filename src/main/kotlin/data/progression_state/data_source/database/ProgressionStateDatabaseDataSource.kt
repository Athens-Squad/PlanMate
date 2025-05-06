package data.progression_state.data_source.database

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import logic.entities.ProgressionState
import net.thechance.data.progression_state.dto.ProgressionStateDto

class ProgressionStateDatabaseDataSource(
	progressionStatesDocument: MongoCollection<ProgressionStateDto>
) : ProgressionStateDataSource {
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