package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository


class GetTaskByIdUseCase(private val taskRepository: TasksRepository) {
    fun execute(taskId: String): Result<Task> {
        return taskRepository.getTaskById(taskId)
    }
}