@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.validators.taskvalidations

import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TaskValidator {
	fun validateTaskFieldsNotBlank(task: Task): Boolean
	suspend fun validateProjectExists(projectId: Uuid): Boolean
	suspend fun validateProgressionStateExists(progressionStateId: Uuid): Boolean
	suspend fun validateTaskNotExists(taskId: Uuid): Boolean
	suspend fun validateTaskAlreadyExists(taskId: Uuid): Boolean
}