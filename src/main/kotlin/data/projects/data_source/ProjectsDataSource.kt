package data.projects.data_source

import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ProjectsDataSource {
    suspend fun createProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: Uuid)
    suspend fun getProjects(): List<Project>
}