package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import net.thechance.exceptions.TasksException
import net.thechance.logic.entities.EntityType
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class UpdateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {

    fun execute(updatedTask: Task, userId: String): Result<Unit> {
        return runCatching {
            taskValidator.doIfTaskExistsOrThrow(updatedTask.id) {
                taskRepository.updateTask(updatedTask)
                    .onSuccess {
                        auditRepository.createAuditLog(
                            auditLog = AuditLog(
                                entityType = EntityType.TASK,
                                entityId = updatedTask.id,
                                description = "Task updated successfully.",
                                userId = userId,
                                createdAt = LocalDateTime.now()
                            )
                        )
                    }
            }
        }
    }

}
