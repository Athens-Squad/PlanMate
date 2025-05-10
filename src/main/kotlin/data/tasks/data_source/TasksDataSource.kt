@file:OptIn(ExperimentalUuidApi::class)

package data.tasks.data_source

import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TasksDataSource {
    suspend fun createTask(task: Task)

    suspend fun getTaskById(taskId: Uuid): Task

    suspend fun getTasksByProjectId(projectId: Uuid): List<Task>

    suspend fun getAllTasks(): List<Task>

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: Uuid)
}