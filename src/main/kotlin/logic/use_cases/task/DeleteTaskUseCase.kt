package com.thechance.logic.usecases.task

import logic.repositories.AuditRepository
import com.thechance.logic.repositories.TasksRepository


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(taskId: Int, projectInt: String, userName: String): Boolean {
        return false
    }
}
