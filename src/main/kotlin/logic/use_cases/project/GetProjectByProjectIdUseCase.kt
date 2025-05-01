package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository


class GetProjectByProjectIdUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    fun execute(username: String,projectId: String): Result<Project> {
        return runCatching {
            projectRepository.getProjects().getOrThrow().first()
        }
    }
}