package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoCollection
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.progression_state.data_source.ProgressionStateDataSource
import data.progression_state.data_source.database.ProgressionStateDatabaseDataSource
import data.tasks.data_source.TasksDataSource
import data.tasks.data_source.TasksFileDataSource
import data.user.data_source.UsersDataSource
import data.user.data_source.UsersFileDataSource
import logic.entities.*
import net.thechance.data.aduit_log.data_source.MongoAuditLogDataSource
import net.thechance.data.aduit_log.dto.AuditLogDto
import net.thechance.data.authentication.UserSession
import net.thechance.data.progression_state.dto.ProgressionStateDto
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.datasource.localcsvfile.ProjectsFileDataSource
import net.thechance.data.projects.datasource.remote.mongo.MongoProjectDataSource
import net.thechance.data.projects.dto.ProjectDto
import net.thechance.data.user.data_source.remote.UserDto
import net.thechance.data.user.data_source.remote.UserMongoDataSource
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
        UserMongoDataSource(get<MongoCollection<UserDto>>())
    }

    single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }
    single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }
    single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLog.Companion::fromCsv) }
    single<AuditLogDataSource> {
        MongoAuditLogDataSource(get<MongoCollection<AuditLogDto>>())
    }

    single(named("projectsCsvFile")) { File("data_files/projects.csv") }
    single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsCsvFile"))) }
    single(named("projectsFileParser")) { CsvFileParser(factory = Project.Companion::fromCsv) }
    single<ProjectsDataSource> {
        MongoProjectDataSource(
            projectsCollection = get<MongoCollection<ProjectDto>>(),
            tasksDataSource = get<TasksDataSource>(),
            statesDataSource = get<ProgressionStateDataSource>()
        )
    }

	single<ProgressionStateDataSource> {
		ProgressionStateDatabaseDataSource(
			progressionStatesCollection = get()
		)
	}
}