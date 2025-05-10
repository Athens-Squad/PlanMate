@file:OptIn(ExperimentalUuidApi::class)

package logic.repositories

import logic.entities.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TasksRepository {
    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: Uuid)

    suspend fun getTaskById(taskId: Uuid): Task

    suspend fun getTasksByProjectId(projectId: Uuid): List<Task>

    suspend fun getAllTasks(): List<Task>
}
