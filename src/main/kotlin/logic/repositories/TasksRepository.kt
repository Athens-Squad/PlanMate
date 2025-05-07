package logic.repositories

import logic.entities.Task

interface TasksRepository {
    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: String)

    suspend fun getTaskById(taskId: String): Task

    suspend fun getTasksByProjectId(projectId: String): List<Task>

    suspend fun getAllTasks(): List<Task>
}
