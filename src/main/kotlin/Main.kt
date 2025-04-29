package net.thechance

import logic.entities.Task
import net.thechance.data.tasks.csv_file_handle.CsvFileHandler
import net.thechance.data.tasks.csv_file_handle.CsvParser
import net.thechance.data.tasks.data_source.TasksFileDataSource
import java.io.File

fun main() {
    val csvParser = CsvParser(Task.Companion::fromCsv)
    val tasksFileHandler = CsvFileHandler(File("tasks.csv"))
    val dataSource = TasksFileDataSource(tasksFileHandler = tasksFileHandler, csvParser)
    val task = Task(
        title = "Task 1",
        description = "Description 1",
        currentState = net.thechance.logic.entities.State(
            id = "1",
            name = "State 1",
            projectId = "1"
        ),
        projectId = "adadascads"
    )
    dataSource.createTask(task)



}