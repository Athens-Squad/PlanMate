package net.thechance.data.projects

import logic.entities.Project
import logic.repositories.ProjectsRepository

class ProjectsRepositoryImpl : ProjectsRepository {
    override fun createProject(project: Project): Result<Unit> {
        return runCatching {  }
    }

    override fun updateProject(project: Project): Result<Unit> {
        return runCatching {  }
    }

    override fun deleteProject(projectId: String): Result<Unit> {
        return runCatching {  }
    }

    override fun getProjects(): Result<List<Project>> {
        return runCatching { emptyList() }
    }
}