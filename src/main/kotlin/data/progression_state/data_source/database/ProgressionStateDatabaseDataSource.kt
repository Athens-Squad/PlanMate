package data.progression_state.data_source.database

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import net.thechance.data.progression_state.dto.ProgressionStateDto

class ProgressionStateDatabaseDataSource(
	progressionStatesDocument: MongoCollection<ProgressionStateDto>
) : ProgressionStateDataSource {
	override suspend fun createProgressionState(progressionState: ProgressionStateDto) {
		TODO("Not yet implemented")
	}

	override suspend fun updateProgressionState(progressionState: ProgressionStateDto) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		TODO("Not yet implemented")
	}

	override suspend fun getProgressionStates(): List<ProgressionStateDto> {
		TODO("Not yet implemented")
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionStateDto> {
		TODO("Not yet implemented")
	}

}