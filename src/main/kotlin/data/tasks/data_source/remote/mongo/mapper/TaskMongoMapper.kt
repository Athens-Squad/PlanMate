@file:OptIn(ExperimentalUuidApi::class)

package data.tasks.data_source.remote.mongo.mapper

import logic.entities.Task
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import kotlin.uuid.ExperimentalUuidApi

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