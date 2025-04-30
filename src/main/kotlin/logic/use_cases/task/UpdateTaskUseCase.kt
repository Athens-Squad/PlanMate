package logic.use_cases.task

import logic.entities.Task
import logic.repositories.TasksRepository


class UpdateTaskUseCase(private val taskRepository: TasksRepository) {
    fun execute(taskId: String, updatedTask: Task): Boolean {
        return false
    }
}
