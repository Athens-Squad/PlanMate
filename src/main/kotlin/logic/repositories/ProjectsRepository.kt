package logic.repositories

import logic.entities.Project

interface ProjectsRepository {
    suspend fun createProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun getProjects(): List<Project>
}