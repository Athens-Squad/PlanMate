package logic.use_cases.project

import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TasksRepository


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val statesRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(projectId: String): Boolean {
        return false
    }
}
