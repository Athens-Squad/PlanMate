package logic.use_cases.project

import logic.entities.Project
import logic.entities.User
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.entities.UserType


class GetProjectByIdUseCase(
    private val projectRepository: ProjectsRepository,
    private val taskRepository: ProjectsRepository,
    private val statesRepository: StatesRepository
) {
    fun execute(projectId: String): Project? {
        return null
    }
}