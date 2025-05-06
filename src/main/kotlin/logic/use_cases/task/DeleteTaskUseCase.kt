package logic.use_cases.task

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.entities.EntityType
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {
    fun execute(taskId: String, userName: String) {
        taskValidator.doIfTaskExistsOrThrow(taskId) {
            // Delete the task
            taskRepository.deleteTask(taskId)

            // Create an audit log for task deletion
            createLog(taskId, userName)
        }
    }

    private fun createLog(taskId: String, userName: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = taskId,
            description = "Task with ID $taskId deleted successfully.",
            userName = userName,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog)
    }
}
