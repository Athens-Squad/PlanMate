package logic.repositories

import logic.entities.Task

interface TasksRepository {
    fun createTask(task: Task)

    fun updateTask(updatedTask: Task)

    fun deleteTask(taskId: String)

    fun getTaskById(taskId: String): Task

    fun getTasksByProjectId(projectId: String): List<Task>

    fun getAllTasks(): List<Task>
}
