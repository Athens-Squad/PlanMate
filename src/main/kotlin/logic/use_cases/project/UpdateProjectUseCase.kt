package logic.use_cases.project

import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import logic.repositories.TasksRepository
import logic.repositories.ProjectsRepository


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val statesRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(updatedProject: Project): Result<Unit> {
        return Result.failure(Exception())
    }
}

