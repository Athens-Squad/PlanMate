package logic.use_cases.task

import logic.repositories.AuditRepository
import logic.repositories.TasksRepository


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(taskId: String): Boolean {
        return false
    }
}
