package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TasksRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectsRepository,
    private val tasksRepository: TasksRepository,
    private val statesRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(project: Project): Boolean {
        return false
    }
}
