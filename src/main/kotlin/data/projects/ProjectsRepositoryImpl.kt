package data.projects

import logic.entities.Project
import logic.repositories.ProjectsRepository

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository {
    override fun createProject(project: Project): Result<Unit> {
        return projectsDataSource.saveProjectInCsvFile(project)
    }

    override fun updateProject(project: Project): Result<Unit> {
        return projectsDataSource.updateProjectFromCsvFile(project)
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        return projectsDataSource.deleteProjectFromCsvFile(projectId)
    }

    override fun getProjects(): Result<List<Project>> {
        return projectsDataSource.getProjectsFromCsvFile()
    }
}