@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetTasksByProjectIdUseCase(private val taskRepository: TasksRepository) {
    suspend fun execute(projectId: Uuid): List<Task> {
        return taskRepository.getTasksByProjectId(projectId)
    }
}
