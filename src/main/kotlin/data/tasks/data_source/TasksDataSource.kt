package data.tasks.data_source

import logic.entities.Task

interface TasksDataSource {
    fun createTask(task: Task): Result<Unit>

    fun getTaskById(taskId: String): Result<Task>

    fun getTasksByProjectId(projectId: String): Result<List<Task>>

    fun getAllTasks(): Result<List<Task>>

    fun updateTask(updatedTask: Task): Result<Unit>

    fun deleteTask(taskId: String): Result<Unit>
}