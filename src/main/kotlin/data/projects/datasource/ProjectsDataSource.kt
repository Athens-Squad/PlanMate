package net.thechance.data.projects.datasource

import logic.entities.Project

interface ProjectsDataSource {
    suspend fun createProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: String)
    suspend fun getProjects(): List<Project>
}