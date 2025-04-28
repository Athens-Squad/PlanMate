package logic.repositories

import logic.entities.Project

interface ProjectsRepository {
    fun createProject(project: Project)
    fun updateProject(project: Project)
    fun deleteProject(projectId: String)
    fun getProjects(): List<Project>
}