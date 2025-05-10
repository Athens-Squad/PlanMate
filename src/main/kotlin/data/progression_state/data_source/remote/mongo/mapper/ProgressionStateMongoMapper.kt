@file:OptIn(ExperimentalUuidApi::class)

package data.progression_state.data_source.remote.mongo.mapper

import logic.entities.ProgressionState
import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import kotlin.uuid.ExperimentalUuidApi


fun ProgressionStateDto.toProgressionState(): ProgressionState {
	return ProgressionState(
		id = id,
		name = name,
		projectId = projectId
	)
}

fun ProgressionState.toProgressionStateDto(): ProgressionStateDto {
	return ProgressionStateDto(
		id = id,
		name = name,
		projectId = projectId
	)
}