package data.projects

import logic.entities.Project
import logic.repositories.ProjectsRepository

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun createProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
    }
}