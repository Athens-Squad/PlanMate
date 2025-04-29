package logic.use_cases.task

import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository

class CreateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(task: Task): Boolean {
        return false
    }
}

