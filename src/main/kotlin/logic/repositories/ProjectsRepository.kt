package logic.repositories

import logic.entities.Project

interface ProjectsRepository {
    fun createProject(project: Project): Result<Unit>
    fun updateProject(project: Project): Result<Unit>
    fun deleteProject(projectId: String): Result<Unit>
    fun getProjects(): Result<List<Project>>
}