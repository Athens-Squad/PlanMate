package net.thechance.data.projects.datasource.remote.mongo

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.states.data_source.StatesDataSource
import data.tasks.data_source.TasksDataSource
import logic.entities.Project
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.dto.ProjectDto

class MongoProjectDataSource(
    private val collection: MongoCollection<ProjectDto>,
    private val tasksDataSource: TasksDataSource,
    private val statesDataSource: StatesDataSource
): ProjectsDataSource {
    override suspend fun createProject(project: Project) {
        val projectDto = ProjectDto.fromProject(project)

        collection.insertOne(projectDto)
    }

    override suspend fun updateProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(projectId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getProjects(): List<Project> {
        TODO("Not yet implemented")
    }

}