package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.*


class UpdateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val statesRepository: StatesRepository,
    private val tasksRepository: TasksRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(updatedProject: Project): Result<Unit> {
        return runCatching {  }
    }
}

