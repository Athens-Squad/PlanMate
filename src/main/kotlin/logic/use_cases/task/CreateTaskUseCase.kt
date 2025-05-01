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
    fun execute(task: Task, userName: String): Result<Unit> {
        return runCatching {
            taskValidator.doIfTaskNotExistsOrThrow(task) {
                //validate task
                taskValidator.validateTaskBeforeCreation(task)

                //create task
                taskRepository.createTask(task)
                    .onSuccess {
                        //create log
                        createLog(task, userName)
                    }


            }
        }
    }

    private fun createLog(task: Task, userName: String) {
        val auditLog = AuditLog(
            entityType = EntityType.TASK,
            entityId = task.id,
            description = "Task ${task.title} created successfully.",
            userId = userName,
            createdAt = LocalDateTime.now()
        )
        auditRepository.createAuditLog(auditLog).getOrThrow()
    }

}

