@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task.taskvalidations

import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TaskValidator {
    suspend fun doIfTaskExistsOrThrow(taskId: Uuid, action: suspend (Task) -> Unit)

    suspend fun doIfTaskNotExistsOrThrow(task: Task, action: suspend () -> Unit)

    suspend fun validateTaskBeforeCreation(task: Task)

    suspend fun validateTaskBeforeUpdating(task: Task, updatedTask: Task)
}