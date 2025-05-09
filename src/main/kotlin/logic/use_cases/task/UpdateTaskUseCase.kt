package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Task
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class UpdateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val createAuditLogUseCase: CreateAuditLogUseCase,
    private val taskValidator: TaskValidator
) {

    suspend fun execute(updatedTask: Task, userName: String) {

        //update only if task exists
        taskValidator.doIfTaskExistsOrThrow(updatedTask.id) { task ->

            //validate updatedTask
            taskValidator.validateTaskBeforeUpdating(task, updatedTask)

            taskRepository.updateTask(updatedTask)

            //create log
            createAuditLogUseCase.execute(
                AuditLog(
                    entityType = EntityType.TASK,
                    entityId = task.id,
                    description = "Project updated successfully.",
                    userName = userName,
                    createdAt = LocalDateTime.now(),
                )
            )
        }
    }
}
