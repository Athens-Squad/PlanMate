@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.tasks.data_source.remote.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.tasks.data_source.TasksDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.entities.Task
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import data.tasks.data_source.remote.mongo.mapper.toTask
import data.tasks.data_source.remote.mongo.mapper.toTaskDto
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoTaskDataSource(
    private val taskCollection: MongoCollection<TaskDto>
) : TasksDataSource {
    override suspend fun createTask(task: Task) {
        val taskDto = task.toTaskDto()
        taskCollection.insertOne(taskDto)
    }

    override suspend fun getTaskById(taskId: Uuid): Task {
        val queryParams = Filters.eq("_id", taskId)
        val taskDto = taskCollection.find(queryParams).firstOrNull()?: throw NoSuchElementException("No task found with ID: $taskId")
        return taskDto.toTask()
    }

    override suspend fun getTasksByProjectId(projectId: Uuid): List<Task> {
        val queryParams = Filters.eq("projectId", projectId)
        val taskDto = taskCollection.find(queryParams).map {
            it.toTask()
        }
        return taskDto.toList()
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskCollection.find<TaskDto>()
            .map { it.toTask() }
            .toList()

    }
    override suspend fun updateTask(task: Task) {
        val updatedTaskDto = task.toTaskDto()
        val queryParams = Filters.eq("_id", task.id)

        taskCollection.replaceOne(queryParams, updatedTaskDto)

    }

    override suspend fun deleteTask(taskId: Uuid) {
        val queryParams = Filters.eq("_id", taskId)
        taskCollection.deleteOne(queryParams)
    }
}