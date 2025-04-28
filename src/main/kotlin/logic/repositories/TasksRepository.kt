package com.thechance.logic.repositories

import logic.entities.Task

interface TasksRepository {
    fun createTask(task: Task)
    fun updateTask(task: Task)
    fun deleteTask(taskId: String)
    fun getTasks(): List<Task>
}
