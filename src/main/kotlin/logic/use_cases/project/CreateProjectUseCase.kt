package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.*
import net.thechance.logic.entities.UserType

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val statesRepository: StatesRepository,
    private val tasksRepository: TasksRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(project: Project): Result<Unit> {
        return runCatching {
        }
    }
}
