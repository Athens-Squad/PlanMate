package net.thechance.logic.use_cases.task

import logic.entities.Task
import com.thechance.logic.repositories.TasksRepository


class GetTaskByIdUseCase(private val taskRepository: TasksRepository) {
    fun execute(taskId: Int, projectId: String): Task? {
        return null
    }
}