package data.tasks.data_source

import logic.entities.Task

interface TasksDataSource {
    fun createTask(task: Task)

    fun getTaskById(taskId: String): Task

    fun getTasksByProjectId(projectId: String): List<Task>

    fun getAllTasks(): List<Task>

    fun updateTask(updatedTask: Task)

    fun deleteTask(taskId: String)
}