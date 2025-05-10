@file:OptIn(ExperimentalUuidApi::class)

package data.projects

import logic.entities.Project
import logic.repositories.ProjectsRepository
import data.projects.data_source.ProjectsDataSource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {
    override suspend fun createProject(project: Project) {
        projectsDataSource.createProject(project)
    }

    override suspend fun updateProject(project: Project) {
        projectsDataSource.updateProject(project)
    }

    override suspend fun deleteProject(projectId: Uuid) {
        projectsDataSource.deleteProject(projectId)
    }

    override suspend fun getProjects(): List<Project> {
       return projectsDataSource.getProjects()
    }
}