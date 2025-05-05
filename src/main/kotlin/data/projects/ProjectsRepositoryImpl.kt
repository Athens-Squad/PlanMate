package data.projects

import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.data.projects.datasource.ProjectsDataSource

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {
    override suspend fun createProject(project: Project) {
        projectsDataSource.createProject(project)
    }

    override suspend fun updateProject(project: Project) {
        projectsDataSource.updateProject(project)
    }

    override suspend fun deleteProject(projectId: String) {
        projectsDataSource.deleteProject(projectId)
    }

    override suspend fun getProjects(): List<Project> {
       return projectsDataSource.getProjects()
    }
}