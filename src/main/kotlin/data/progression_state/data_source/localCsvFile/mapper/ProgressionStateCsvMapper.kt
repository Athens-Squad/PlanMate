@file:OptIn(ExperimentalUuidApi::class)

package data.progression_state.data_source.localCsvFile.mapper

import data.progression_state.data_source.localCsvFile.dto.ProgressionStateCsvDto
import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun ProgressionStateCsvDto.toProgressionState(): ProgressionState {
    return ProgressionState(
        id = Uuid.random(),
        name = name,
        projectId = Uuid.random()
    )
}

fun ProgressionState.toProgressionStateCsvDto(): ProgressionStateCsvDto {
    return ProgressionStateCsvDto(
        id = Uuid.random(),
        name = name,
        projectId = Uuid.random()
    )
}