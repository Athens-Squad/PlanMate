package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository

class GetProjectsByUsernameUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    fun execute(username: String, projectCreator: String): Result<List<Project>> {
        return runCatching {
            emptyList()
        }
    }
}