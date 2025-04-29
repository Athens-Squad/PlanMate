package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import net.thechance.logic.entities.EntityType
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime

class CreateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {
    fun execute(task: Task, userId: String): Result<Unit> {
        return runCatching {
            taskValidator.doIfTaskExistsOrThrow(task.id) {
                //validate task
                taskValidator.validateTaskBeforeCreation(task)

                //create task
                taskRepository.createTask(task).getOrThrow()

                //create log
                createLog(task, userId)
            }
        }
    }

    private fun createLog(task: Task, userId: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = task.id,
            description = "Task ${task.title} created successfully.",
            userId = userId,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog).getOrThrow()
    }

}

