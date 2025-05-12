@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository
import logic.use_cases.task.taskvalidations.TaskValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetTaskByIdUseCase(
	private val taskRepository: TasksRepository,
	private val taskValidator: TaskValidator
) {
    suspend fun execute(taskId: Uuid): Task {
		taskValidator.validateTaskAlreadyExists(taskId)
        return taskRepository.getTaskById(taskId)
    }
}