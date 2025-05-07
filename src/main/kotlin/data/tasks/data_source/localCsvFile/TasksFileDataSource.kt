package net.thechance.data.tasks.data_source.localCsvFile

import logic.entities.Task
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.tasks.data_source.TasksDataSource

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

    override suspend fun updateTask(task: Task) {
        val updatedTasks = getAllTasks()
            .map { if (it.id == task.id) task else it }
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