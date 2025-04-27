package com.thechance.logic.usecases.task

import logic.entities.Task
import com.thechance.logic.repositories.TasksRepository


class UpdateTaskUseCase(private val taskRepository: TasksRepository) {
    fun execute(taskId: String, updatedTask: Task): Boolean {
        return false
    }
}
