package net.thechance.logic.use_cases.task.taskvalidations

import logic.entities.Task

interface TaskValidator {
    fun doIfTaskExistsOrThrow(taskId: String, action: () -> Unit)

    fun doIfTaskNotExistsOrThrow(task: Task, action: () -> Unit)

    fun validateTaskBeforeCreation(task: Task)

    fun validateTaskBeforeUpdating(task: Task, updatedTask: Task)
}