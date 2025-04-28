package net.thechance.logic.use_cases.project

import logic.repositories.StatesRepository
import com.thechance.logic.repositories.TasksRepository
import logic.entities.Project
import logic.repositories.ProjectsRepository

class GetProjectsByUserIdUseCase(
    private val projectRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val statesRepository: StatesRepository,
) {
    fun execute(userName: String): List<Project> {

        return emptyList()
    }
}