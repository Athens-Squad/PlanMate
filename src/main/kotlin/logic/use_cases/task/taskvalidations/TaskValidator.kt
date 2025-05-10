package logic.use_cases.task.taskvalidations

import logic.entities.Task

interface TaskValidator {
	suspend fun validateTaskBeforeCreation(task: Task): Boolean
	suspend fun validateTaskAfterCreation(taskId: String): Boolean
}