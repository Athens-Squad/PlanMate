package net.thechance.logic.use_cases.task

import logic.entities.Task
import com.thechance.logic.repositories.TasksRepository


class GetTasksByProjectIdUseCase(private val taskRepository: TasksRepository) {
    fun execute(projectId: String): List<Task> {
        return emptyList()
    }
}
