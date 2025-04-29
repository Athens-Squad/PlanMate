package logic.use_cases.task

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import net.thechance.logic.entities.EntityType
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {
    fun execute(taskId: String, userId: String): Result<Unit> {
        return runCatching {
            taskValidator.doIfTaskExistsOrThrow(taskId) {
                // Delete the task
                taskRepository.deleteTask(taskId)
                    .onSuccess {
                        // Create an audit log for task deletion
                        createLog(taskId, userId)
                    }
            }
        }
    }

    private fun createLog(taskId: String, userId: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = taskId,
            description = "Task with ID $taskId deleted successfully.",
            userId = userId,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog).getOrThrow()
    }
}
