package data.tasks.data_source

import logic.entities.Task

interface TasksDataSource {
    suspend fun createTask(task: Task)

    suspend fun getTaskById(taskId: String): Task

    suspend fun getTasksByProjectId(projectId: String): List<Task>

    suspend fun getAllTasks(): List<Task>

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: String)
}