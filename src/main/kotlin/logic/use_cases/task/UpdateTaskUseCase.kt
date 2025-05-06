package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.entities.EntityType
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class UpdateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {

    suspend fun execute(updatedTask: Task, userName: String) {

        //update only if task exists
        taskValidator.doIfTaskExistsOrThrow(updatedTask.id) { task ->

            //validate updatedTask
            taskValidator.validateTaskBeforeUpdating(task, updatedTask)

            taskRepository.updateTask(updatedTask)

            //create log
            createLog(updatedTask, userName)

        }
    }

    private suspend fun createLog(task: Task, userName: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = task.id,
            description = "Task ${task.title} updated successfully.",
            userName = userName,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog)
    }

}
