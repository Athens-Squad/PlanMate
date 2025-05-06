package logic.use_cases.task.taskvalidations

import logic.entities.Task

interface TaskValidator {
    suspend fun doIfTaskExistsOrThrow(taskId: String, action: (Task) -> Unit)

    suspend fun doIfTaskNotExistsOrThrow(task: Task, action: () -> Unit)

    suspend fun validateTaskBeforeCreation(task: Task)

    suspend fun validateTaskBeforeUpdating(task: Task, updatedTask: Task)
}