package net.thechance.data.tasks.repository

import logic.entities.Task
import logic.repositories.TasksRepository
import net.thechance.data.tasks.data_source.TasksDataSource

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource
): TasksRepository {
    override fun createTask(task: Task): Result<Unit> {
        return tasksDataSource.createTask(task)
    }

    override fun updateTask(task: Task): Result<Unit> {
        return tasksDataSource.updateTask(task)
    }

    override fun deleteTask(taskId: String): Result<Unit> {
       return tasksDataSource.deleteTask(taskId)
    }

    override fun getTaskById(taskId: String): Result<Task> {
        return tasksDataSource.getTaskById(taskId)
    }

    override fun getTasksByProjectId(projectId: String): Result<List<Task>> {
        return tasksDataSource.getTasksByProjectId(projectId)
    }

    override fun getTasks(): Result<List<Task>> {
        return tasksDataSource.getAllTasks()
    }
}