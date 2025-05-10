package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import data.progression_state.data_source.remote.mongo.ProgressionStateDatabaseDataSource
import data.tasks.data_source.TasksDataSource
import data.user.data_source.UsersDataSource
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import net.thechance.data.aduit_log.data_source.remote.mongo.MongoAuditLogDataSource
import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import net.thechance.data.authentication.UserSession
import data.projects.data_source.ProjectsDataSource
import data.projects.data_source.remote.mongo.MongoProjectDataSource
import data.projects.data_source.localcsvfile.dto.ProjectCsvDto
import net.thechance.data.tasks.data_source.remote.mongo.MongoTaskDataSource
import net.thechance.data.tasks.data_source.localCsvFile.dto.TaskCsvDto
import net.thechance.data.user.data_source.remote.mongo.UserMongoDataSource
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto
import net.thechance.data.user.data_source.remote.UserMongoDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule = module {
	single(named("tasksCsvFile")) { File("data_files/tasks.csv") }
	single(named("tasksFileHandler")) { CsvFileHandler(get(named("tasksCsvFile"))) }
	single(named("tasksFileParser")) { CsvFileParser(factory = TaskCsvDto.Companion::fromCsv) }
	single<TasksDataSource> {
		MongoTaskDataSource(
			taskCollection = get(named("tasksCollection"))
		)
	}

	single(named("usersCsvFile")) { File("data_files/users.csv") }
	single(named("usersFileHandler")) { CsvFileHandler(get(named("usersCsvFile"))) }
	single(named("usersFileParser")) { CsvFileParser(factory = UserCsvDto.Companion::fromCsv) }
	single { UserSession() }
	single<UsersDataSource> {
		UserMongoDataSource(get<MongoCollection<UserDto>>())
	}
	single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }
	single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }
	single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLogCsvDto.Companion::fromCsv) }
	single<AuditLogDataSource> {
		MongoAuditLogDataSource(get(named("auditLogsCollection")))
	}

	single(named("projectsCsvFile")) { File("data_files/projects.csv") }
	single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsCsvFile"))) }
	single(named("projectsFileParser")) { CsvFileParser(factory = ProjectCsvDto.Companion::fromCsv) }
	single<ProjectsDataSource> {
		MongoProjectDataSource(
			projectsCollection = get(named("projectsCollection")),
			tasksDataSource = get<TasksDataSource>(),
			statesDataSource = get<ProgressionStateDataSource>()
		)
	}

	single<ProgressionStateDataSource> {
		ProgressionStateDatabaseDataSource(
			progressionStatesCollection = get(named("progressionStatesCollection"))
		)
	}
}