package net.thechance.data.tasks.data_source

import logic.entities.Task
import net.thechance.data.tasks.csv_file_handle.TasksFileHandler
import net.thechance.data.tasks.csv_file_handle.TasksFileParser

class TasksFileDataSource(
    private val tasksFileHandler: TasksFileHandler,
    private val tasksFileParser: TasksFileParser
): TasksDataSource {

    override fun createTask(task: Task): Result<Unit> {
        return runCatching {
            val record = tasksFileParser.taskToCsvRecord(task)
            tasksFileHandler.appendRecord(record)
        }
    }

    override fun getTaskById(taskId: String): Result<Task> {
        return runCatching {
            getAllTasks()
                .getOrThrow()
                .firstOrNull {it.id == taskId}
                ?: throw NoSuchElementException("No such Task with taskId: $taskId")
        }
    }

    override fun getTasksByProjectId(projectId: String): Result<List<Task>> {
        return runCatching {
            getAllTasks()
                .getOrThrow()
                .filter { it.projectId == projectId }
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        return runCatching {
            tasksFileHandler.readRecords()
                .map { tasksFileParser.parseRecord(it) }
        }
    }

    override fun updateTask(updatedTask: Task): Result<Unit> {
        return runCatching {
            val updatedTasks = getAllTasks()
                .getOrThrow()
                .map { if (it.id == updatedTask.id) updatedTask else it }
            val updatedRecords = updatedTasks.map { tasksFileParser.taskToCsvRecord(it) }
            tasksFileHandler.writeRecords(updatedRecords)
        }
    }

    override fun deleteTask(taskId: String): Result<Unit> {
        return runCatching {
            val updatedTasks = getAllTasks()
                .getOrThrow()
                .filter { it.id != taskId }
            val updatedRecords = updatedTasks.map { tasksFileParser.taskToCsvRecord(it) }
            tasksFileHandler.writeRecords(updatedRecords)
        }
    }
}