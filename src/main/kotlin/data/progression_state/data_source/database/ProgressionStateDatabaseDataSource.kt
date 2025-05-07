package data.progression_state.data_source.database

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import kotlinx.coroutines.flow.toList
import logic.entities.ProgressionState
import net.thechance.data.progression_state.dto.ProgressionStateDto
import net.thechance.data.progression_state.mappers.toProgressionState
import net.thechance.data.progression_state.mappers.toProgressionStateDto

class ProgressionStateDatabaseDataSource(
	private val progressionStatesCollection: MongoCollection<ProgressionStateDto>
) : ProgressionStateDataSource {
	override suspend fun createProgressionState(progressionState: ProgressionState) {
		progressionStatesCollection.insertOne(progressionState.toProgressionStateDto())
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		val queryById = Filters.eq(progressionState.id)
		val updateProgressionStateFields = Updates.combine(
			Updates.set(ProgressionState::name.name, progressionState.name),
			Updates.set(ProgressionState::projectId.name, progressionState.projectId)
		)
		val updateOptions = UpdateOptions().upsert(true)
		progressionStatesCollection.updateOne(
			filter = queryById,
			update = updateProgressionStateFields,
			options = updateOptions
		)
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		progressionStatesCollection.deleteOne(
			filter = Filters.eq("_id", progressionStateId)
		)
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		return progressionStatesCollection.find().toList()
			.map { it.toProgressionState() }
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		return progressionStatesCollection.find(Filters.eq("projectId", projectId))
			.toList()
			.map { it.toProgressionState() }
	}
}