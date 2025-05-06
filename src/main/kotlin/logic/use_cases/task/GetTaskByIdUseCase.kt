package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository


class GetTaskByIdUseCase(private val taskRepository: TasksRepository) {
    suspend fun execute(taskId: String): Task {
        return taskRepository.getTaskById(taskId)
    }
}