package logic.repositories

import logic.entities.Task

interface TasksRepository {

    fun createTask(task: Task): Result<Unit>

    fun updateTask(task: Task): Result<Unit>

    fun deleteTask(taskId: String): Result<Unit>

    fun getTaskById(taskId: String): Result<Task>

    fun getTasksByProjectId(projectId: String): Result<List<Task>>

    fun getTasks(): Result<List<Task>>
}
