package net.thechance.data.tasks.data_source.localCsvFile.mapper

import logic.entities.Task
import net.thechance.data.tasks.data_source.localCsvFile.dto.TaskCsvDto

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