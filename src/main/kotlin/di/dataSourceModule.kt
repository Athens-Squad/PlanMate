package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.aduit_log_csvfile.data_source.AuditLogDataSource
import data.aduit_log_csvfile.data_source.AuditLogFileDataSource
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.datasource.localcsvfile.ProjectsFileDataSource
import data.states.data_source.StatesDataSource
import data.states.data_source.StatesFileDataSource
import data.tasks.data_source.TasksDataSource
import data.tasks.data_source.TasksFileDataSource
import data.user.data_source.UsersDataSource
import data.user.data_source.UsersFileDataSource
import logic.entities.*
import net.thechance.data.authentication.UserSession
import net.thechance.data.projects.datasource.remote.mongo.MongoProjectDataSource
import net.thechance.data.projects.dto.ProjectDto
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule  = module {
    single(named("tasksCsvFile")) { File("data_files/tasks.csv") }
    single(named("tasksFileHandler")) { CsvFileHandler(get(named("tasksCsvFile"))) }
    single(named("tasksFileParser")) { CsvFileParser(factory = Task.Companion::fromCsv) }
    single<TasksDataSource> {
        TasksFileDataSource(
            tasksFileHandler = get(named("tasksFileHandler")),
            csvFileParser = get(named("tasksFileParser"))
        )
    }

    single(named("usersCsvFile")) { File("data_files/users.csv") }
    single(named("usersFileHandler")) { CsvFileHandler(get(named("usersCsvFile"))) }
    single(named("usersFileParser")) { CsvFileParser(factory = User.Companion::fromCsv) }
    single { UserSession() }
    single<UsersDataSource> {
        UsersFileDataSource(
            userFileHandler = get(named("usersFileHandler")),
            csvFileParser = get(named("usersFileParser"))
        )
    }

    single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }
    single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }
    single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLog.Companion::fromCsv) }
    single<AuditLogDataSource> {
        AuditLogFileDataSource(
            auditLogFileHandler = get(named("AuditLogFileHandler")),
            csvFileParser = get(named("AuditLogFileParser"))
        )
    }

    single(named("projectsCsvFile")) { File("data_files/projects.csv") }
    single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsCsvFile"))) }
    single(named("projectsFileParser")) { CsvFileParser(factory = Project.Companion::fromCsv) }
    single<ProjectsDataSource> {
        MongoProjectDataSource(get<MongoCollection<ProjectDto>>(), get(), get())
    }

    single(named("stateCsvFile")) { File("data_files/states.csv") }
    single(named("statesFileHandler")) { CsvFileHandler(get(named("stateCsvFile"))) }
    single(named("statesFileParser")) { CsvFileParser(factory = ProgressionState.Companion::fromCsv) }
    single<StatesDataSource> {
        StatesFileDataSource(
            statesFileHandler = get(named("statesFileHandler")),
            csvFileParser = get(named("statesFileParser"))
        )
    }
}