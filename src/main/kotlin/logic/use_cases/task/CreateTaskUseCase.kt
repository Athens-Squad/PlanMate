package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.entities.EntityType
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime
import logic.use_cases.project.log_builder.createLog

class CreateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {
    suspend fun execute(task: Task, userName: String) {
        taskValidator.doIfTaskNotExistsOrThrow(task) {
            //validate task
            taskValidator.validateTaskBeforeCreation(task)

            //create task
            taskRepository.createTask(task)
            createLog(task, userName)

        }
    }


private suspend fun createLog(task: Task, userName: String) {
    val auditLog = AuditLog(
        entityType = EntityType.TASK,
        entityId = task.id,
        description = "Task ${task.title} created successfully.",
        userName = userName,
        createdAt = LocalDateTime.now()
    )
    auditRepository.createAuditLog(auditLog)
}


}