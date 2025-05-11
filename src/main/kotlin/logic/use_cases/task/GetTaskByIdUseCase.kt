@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetTaskByIdUseCase(private val taskRepository: TasksRepository) {
    suspend fun execute(taskId: Uuid): Task {
        return taskRepository.getTaskById(taskId)
    }
}