package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.entities.EntityType
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime
import net.thechance.logic.use_cases.audit_log.log_builder.createLog

class CreateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
    private val taskValidator: TaskValidator
) {
    suspend fun execute(task: Task, userName: String) {
        taskValidator.doIfTaskNotExistsOrThrow(task) {
            //validate task
            taskValidator.validateTaskBeforeCreation(task)

            //create task
            taskRepository.createTask(task)

            createLog(
                entityType = EntityType.TASK,
                entityId = task.id,
                logMessage = "Task created successfully.",
                userName = userName,
            ) {
                createAuditLogUseCase.execute(it)
            }

        }
    }
}