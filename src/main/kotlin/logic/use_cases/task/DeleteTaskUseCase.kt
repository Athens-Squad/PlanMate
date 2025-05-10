@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class DeleteTaskUseCase(
    private val taskRepository: TasksRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
    private val taskValidator: TaskValidator
) {
    suspend fun execute(taskId: Uuid, userName: String) {
        taskValidator.doIfTaskExistsOrThrow(taskId) {
            // Delete the task
            taskRepository.deleteTask(taskId)

            // Create an audit log for task deletion
            createAuditLogUseCase.execute(
                AuditLog(
                    entityType = EntityType.TASK,
                    entityId = taskId,
                    description = "Task deleted successfully.",
                    userName = userName,
                    createdAt = LocalDateTime.now(),
                )
            )
        }
    }
}
