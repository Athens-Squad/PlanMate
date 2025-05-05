package net.thechance.data.projects.datasource.remote.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.states.data_source.StatesDataSource
import data.tasks.data_source.TasksDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.entities.ProgressionState
import logic.entities.Project
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.dto.ProjectDto

class MongoProjectDataSource(
    private val projectsCollection: MongoCollection<ProjectDto>,
    private val tasksDataSource: TasksDataSource,
    private val statesDataSource: StatesDataSource
) : ProjectsDataSource {
    override suspend fun createProject(project: Project) {
        val projectDto = ProjectDto.fromProject(project)

        projectsCollection.insertOne(projectDto)
    }

    override suspend fun updateProject(project: Project) {
        val updatedProjectDto = ProjectDto.fromProject(project)

        val queryParams = Filters.eq("_id", project.id)

        projectsCollection.replaceOne(queryParams, updatedProjectDto)
    }

    override suspend fun deleteProject(projectId: String) {
        val queryParams = Filters.eq("_id", projectId)

        projectsCollection.deleteOne(queryParams)
    }

    override suspend fun getProjects(): List<Project> {
        return projectsCollection.find()
            .map { projectDto: ProjectDto ->
                val project = projectDto.toProject()

                val progressionStates = statesDataSource.getStates()
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