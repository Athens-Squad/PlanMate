package logic.use_cases.task

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.entities.EntityType
import logic.use_cases.task.taskvalidations.TaskValidator
import net.thechance.logic.use_cases.audit_log.log_builder.createLog
import java.time.LocalDateTime


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {
    suspend fun execute(taskId: String, userName: String) {
        taskValidator.doIfTaskExistsOrThrow(taskId) {
            // Delete the task
            taskRepository.deleteTask(taskId)

            // Create an audit log for task deletion
            createLog(
                entityType = EntityType.TASK,
                entityId = taskId,
                logMessage = "Task deleted successfully.",
                userName = userName,
            ) {
                auditRepository.createAuditLog(it)
            }
        }
    }
}
