package data.tasks.data_source

import logic.entities.Task
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser

class TasksFileDataSource(
    private val tasksFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<Task>
) : TasksDataSource {

    override suspend fun createTask(task: Task) {
        val record = csvFileParser.toCsvRecord(task)
        tasksFileHandler.appendRecord(record)
    }

    override suspend fun getTaskById(taskId: String): Task {
        return getAllTasks()
            .firstOrNull { it.id == taskId }
            ?: throw NoSuchElementException("No such Task with taskId: $taskId")

    }


    override suspend fun getTasksByProjectId(projectId: String): List<Task> {
        return getAllTasks()
            .filter { it.projectId == projectId }
    }

    override suspend fun getAllTasks(): List<Task> {
        return tasksFileHandler.readRecords()
            .map { csvFileParser.parseRecord(it) }
    }

    override suspend fun updateTask(updatedTask: Task) {
        val updatedTasks = getAllTasks()
            .map { if (it.id == updatedTask.id) updatedTask else it }
        val updatedRecords = updatedTasks.map { csvFileParser.toCsvRecord(it) }
        tasksFileHandler.writeRecords(updatedRecords)

    }

    override suspend fun deleteTask(taskId: String) {
        val updatedTasks = getAllTasks()
            .filter { it.id != taskId }
        val updatedRecords = updatedTasks.map { csvFileParser.toCsvRecord(it) }
        tasksFileHandler.writeRecords(updatedRecords)
    }
}