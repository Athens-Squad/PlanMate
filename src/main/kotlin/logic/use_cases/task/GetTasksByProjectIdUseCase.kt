package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository


class GetTasksByProjectIdUseCase(private val taskRepository: TasksRepository) {
    fun execute(projectId: String): List<Task> {
        return taskRepository.getTasksByProjectId(projectId)
    }
}
