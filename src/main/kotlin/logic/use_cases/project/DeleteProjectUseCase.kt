package net.thechance.logic.use_cases.project

import logic.repositories.*


class DeleteProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(projectId: String): Result<Unit> {
        return runCatching {

        }
    }
}
