package data.tasks.data_source

import logic.entities.Task
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser

class TasksFileDataSource(
    private val tasksFileHandler: CsvFileHandler ,
    private val csvFileParser: CsvFileParser<Task>
): TasksDataSource {

    override fun createTask(task: Task): Result<Unit> {
        return runCatching {
            val record = csvFileParser.toCsvRecord(task)
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
                .map {csvFileParser.parseRecord(it) }
        }
    }

    override fun updateTask(updatedTask: Task): Result<Unit> {
        return runCatching {
            val updatedTasks = getAllTasks()
                .getOrThrow()
                .map { if (it.id == updatedTask.id) updatedTask else it }
            val updatedRecords = updatedTasks.map { csvFileParser.toCsvRecord(it) }
            tasksFileHandler.writeRecords(updatedRecords)
        }
    }

    override fun deleteTask(taskId: String): Result<Unit> {
        return runCatching {
            val updatedTasks = getAllTasks()
                .getOrThrow()
                .filter { it.id != taskId }
            val updatedRecords = updatedTasks.map {  csvFileParser.toCsvRecord(it) }
            tasksFileHandler.writeRecords(updatedRecords)
        }
    }
}