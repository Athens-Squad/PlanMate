package net.thechance.data.tasks.mappers

import logic.entities.Task
import net.thechance.data.tasks.dto.TaskCsvDto
import net.thechance.data.tasks.dto.TaskDto

fun TaskDto.toTask(): Task {
	return Task(
		id = id,
		title = title,
		description = description,
		currentProgressionState = currentProgressionState,
		projectId = projectId
	)
}

fun Task.toTaskDto(): TaskDto {
	return TaskDto(
		id = id,
		title = title,
		description = description,
		currentProgressionState = currentProgressionState,
		projectId = projectId
	)
}

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