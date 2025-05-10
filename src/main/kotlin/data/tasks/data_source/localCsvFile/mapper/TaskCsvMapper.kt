@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.tasks.data_source.localCsvFile.mapper

import logic.entities.Task
import net.thechance.data.tasks.data_source.localCsvFile.dto.TaskCsvDto
import kotlin.uuid.ExperimentalUuidApi

fun TaskCsvDto.toTask(): Task {
	return Task(
		id = id,
		title = title,
		description = description,
		currentProgressionState = currentProgressionState,
		projectId = projectId
	)
}

fun Task.toTaskCsvDto(): TaskCsvDto {
	return TaskCsvDto(
		id = id,
		title = title,
		description = description,
		currentProgressionState = currentProgressionState,
		projectId = projectId
	)
}