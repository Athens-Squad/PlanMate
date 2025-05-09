package net.thechance.data.progression_state.mappers

import logic.entities.ProgressionState
import net.thechance.data.progression_state.dto.ProgressionStateCsvDto
import net.thechance.data.progression_state.dto.ProgressionStateDto

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