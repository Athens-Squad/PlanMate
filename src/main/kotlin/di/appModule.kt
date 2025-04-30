package net.thechance.di

import logic.entities.Project
import logic.entities.Task
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.projects.ProjectsDataSource
import net.thechance.data.projects.ProjectsFileDataSource
import net.thechance.data.tasks.data_source.TasksDataSource
import net.thechance.data.tasks.data_source.TasksFileDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single(named("tasksCsvFile")) { File("src/main/resources/tasks.csv") }
    single(named("projectsCsvFile")) { File("src/main/resources/projects.csv") }

    single(named("tasksFileHandler")) { CsvFileHandler(get(named("tasksCsvFile"))) }
    single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsCsvFile"))) }

    single(named("tasksFileParser")) { CsvFileParser(factory = Task.Companion::fromCsv) }
    single(named("projectsFileParser")) { CsvFileParser(factory = Project.Companion::fromCsv) }


    single<TasksDataSource> {
        TasksFileDataSource(
            tasksFileHandler = get(named("tasksFileHandler")),
            csvFileParser = get(named("tasksFileParser"))
        )
    }

    single<ProjectsDataSource> {
        ProjectsFileDataSource(
            projectsFileHandler = get(named("projectsFileHandler")),
            csvFileParser = get(named("projectsFileParser")),
            tasksFileDataSource = get()
        )
    }
}
