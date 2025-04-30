package net.thechance.di

import logic.entities.Task
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.tasks.data_source.TasksDataSource
import net.thechance.data.tasks.data_source.TasksFileDataSource
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidatorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single(named("tasksCsvFile")) { File("data_files/tasks.csv") }

    single(named("tasksFileHandler")) { CsvFileHandler(get(named("tasksCsvFile"))) }

    single(named("tasksFileParser")) { CsvFileParser(factory = Task.Companion::fromCsv) }


    single<TasksDataSource> {
        TasksFileDataSource(
            tasksFileHandler = get(named("tasksFileHandler")),
            csvFileParser = get(named("tasksFileParser"))
        )
    }

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
}
