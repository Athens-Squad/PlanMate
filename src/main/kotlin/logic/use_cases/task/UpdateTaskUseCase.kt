package logic.use_cases.task

import logic.entities.AuditLog
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import net.thechance.logic.entities.EntityType
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import java.time.LocalDateTime


class UpdateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val auditRepository: AuditRepository,
    private val taskValidator: TaskValidator
) {

    fun execute(updatedTask: Task, userName: String): Result<Unit> {
        return runCatching {
            //update only if task exists
            taskValidator.doIfTaskExistsOrThrow(updatedTask.id) { task ->

                //validate updatedTask
                taskValidator.validateTaskBeforeUpdating(task, updatedTask)

                taskRepository.updateTask(updatedTask)
                    .onSuccess {

                        //create log
                        createLog(updatedTask, userName)

                    }
            }
        }
    }

    private fun createLog(task: Task, userName: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = task.id,
            description = "Task ${task.title} updated successfully.",
            userId = userName,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog).getOrThrow()
    }

}
