@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.remote.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import data.tasks.data_source.TasksDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.entities.ProgressionState
import logic.entities.Project
import data.projects.data_source.ProjectsDataSource
import data.projects.data_source.remote.mongo.dto.ProjectDto
import data.projects.data_source.remote.mongo.mapper.toProject
import data.projects.data_source.remote.mongo.mapper.toProjectDto
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoProjectDataSource(
    private val projectsCollection: MongoCollection<ProjectDto>,
    private val tasksDataSource: TasksDataSource,
    private val statesDataSource: ProgressionStateDataSource
) : ProjectsDataSource {
    override suspend fun createProject(project: Project) {
        val projectDto = project.toProjectDto()

        projectsCollection.insertOne(projectDto)
    }

    override suspend fun updateProject(project: Project) {
        val updatedProjectDto = project.toProjectDto()

        val queryParams = Filters.eq("_id", updatedProjectDto.id)

        projectsCollection.replaceOne(queryParams, updatedProjectDto)
    }

    override suspend fun deleteProject(projectId: Uuid) {
        val queryParams = Filters.eq("_id", projectId.toString())

        projectsCollection.deleteOne(queryParams)
    }

    override suspend fun getProjects(): List<Project> {
        return projectsCollection.find()
            .map { projectDto: ProjectDto ->
                val project = projectDto.toProject()

                val progressionStates = statesDataSource.getProgressionStates()
                    .filter { state: ProgressionState ->
                        state.projectId == project.id
                    }
                    .toMutableList()

                val tasks = tasksDataSource.getTasksByProjectId(project.id).toMutableList()

                project.copy(
                    progressionStates = progressionStates,
                    tasks = tasks
                )
            }
            .toList()
    }

}