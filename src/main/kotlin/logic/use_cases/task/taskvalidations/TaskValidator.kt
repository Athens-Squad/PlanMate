@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task.taskvalidations

import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TaskValidator {
	suspend fun validateTaskBeforeCreation(task: Task): Boolean
	suspend fun validateTaskAfterCreation(taskId: Uuid): Boolean
}