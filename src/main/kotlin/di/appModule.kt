package net.thechance.di

import logic.entities.AuditLog
import logic.entities.Project
import logic.entities.Task
import logic.entities.User
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.projects.ProjectsDataSource
import net.thechance.data.projects.ProjectsFileDataSource
import net.thechance.data.tasks.data_source.TasksDataSource
import net.thechance.data.tasks.data_source.TasksFileDataSource
import net.thechance.data.user.data_source.UsersDataSource
import net.thechance.data.user.data_source.UsersFileDataSource
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

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
    single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }

    single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }

    single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLog.Companion::fromCsv) }


    single<AuditLogDataSource> {
        AuditLogFileDataSource(
            auditLogFileHandler = get(named("AuditLogFileHandler")),
            csvFileParser = get(named("AuditLogFileParser"))
        )
    }

    single<ProjectsDataSource> {
        ProjectsFileDataSource(
            projectsFileHandler = get(named("projectsFileHandler")),
            csvFileParser = get(named("projectsFileParser")),
            tasksFileDataSource = get()
        )
    }

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }

    single(named("usersCsvFile")) { File("data_files/users.csv") }

    single(named("usersFileHandler")) { CsvFileHandler(get(named("usersCsvFile"))) }

    single(named("usersFileParser")) { CsvFileParser(factory = User.Companion::fromCsv) }

    single<UsersDataSource> {
        UsersFileDataSource(
            userFileHandler = get(named("usersFileHandler")),
            csvFileParser = get(named("usersFileParser"))
        )
    }
}
