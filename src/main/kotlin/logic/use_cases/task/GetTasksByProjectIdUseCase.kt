@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository
import logic.use_cases.task.taskvalidations.TaskValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetTasksByProjectIdUseCase(
	private val taskRepository: TasksRepository,
	private val taskValidator: TaskValidator
) {
    suspend fun execute(projectId: Uuid): List<Task> {
		taskValidator.validateProjectExists(projectId)
        return taskRepository.getTasksByProjectId(projectId)
    }
}
