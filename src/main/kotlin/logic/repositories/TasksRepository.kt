package logic.repositories

import logic.entities.Task

interface TasksRepository {
    fun createTask(task: Task): Result<Unit>
    fun updateTask(task: Task): Result<Unit>
    fun deleteTask(taskId: String): Result<Unit>
    fun getTasks(): Result<List<Task>>
}
