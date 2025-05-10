@file:OptIn(ExperimentalUuidApi::class)

package data.progression_state.data_source.localCsvFile.mapper

import logic.entities.ProgressionState
import data.progression_state.data_source.localCsvFile.dto.ProgressionStateCsvDto
import kotlin.uuid.ExperimentalUuidApi

fun ProgressionStateCsvDto.toProgressionState(): ProgressionState {
	return ProgressionState(
		id = id,
		name = name,
		projectId = projectId
	)
}

fun ProgressionState.toProgressionStateCsvDto(): ProgressionStateCsvDto {
	return ProgressionStateCsvDto(
		id = id,
		name = name,
		projectId = projectId
	)
}