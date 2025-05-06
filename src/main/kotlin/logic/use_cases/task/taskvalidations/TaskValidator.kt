package logic.use_cases.task.taskvalidations

import logic.entities.Task

interface TaskValidator {
    suspend fun doIfTaskExistsOrThrow(taskId: String, action: suspend (Task) -> Unit)

    suspend fun doIfTaskNotExistsOrThrow(task: Task, action: suspend () -> Unit)

    suspend fun validateTaskBeforeCreation(task: Task)

    suspend fun validateTaskBeforeUpdating(task: Task, updatedTask: Task)
}