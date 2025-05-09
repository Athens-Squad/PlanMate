package net.thechance.data.tasks.data_source.localCsvFile

import logic.entities.Task
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import data.tasks.data_source.TasksDataSource
import net.thechance.data.tasks.dto.TaskCsvDto
import net.thechance.data.tasks.mappers.toTask
import net.thechance.data.tasks.mappers.toTaskCsvDto

class TasksFileDataSource(
    private val tasksFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<TaskCsvDto>
) : TasksDataSource {

    override suspend fun createTask(task: Task) {
        val record = csvFileParser.toCsvRecord(task.toTaskCsvDto())
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
            .map { csvFileParser.parseRecord(it).toTask() }
    }

    override suspend fun updateTask(task: Task) {
        val updatedTasks = getAllTasks()
            .map { if (it.id == task.id) task else it }
        val updatedRecords = updatedTasks.map { csvFileParser.toCsvRecord(it.toTaskCsvDto()) }
        tasksFileHandler.writeRecords(updatedRecords)

    }

    override suspend fun deleteTask(taskId: String) {
        val updatedTasks = getAllTasks()
            .filter { it.id != taskId }
        val updatedRecords = updatedTasks.map { csvFileParser.toCsvRecord(it.toTaskCsvDto()) }
        tasksFileHandler.writeRecords(updatedRecords)
    }
}