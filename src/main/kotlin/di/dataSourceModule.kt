package net.thechance.di

import data.progression_state.data_source.ProgressionStateDataSource
import data.progression_state.data_source.remote.mongo.ProgressionStateDatabaseDataSource
import data.tasks.data_source.TasksDataSource
import data.user.data_source.UsersDataSource
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import net.thechance.data.aduit_log.data_source.remote.mongo.MongoAuditLogDataSource
import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import data.progression_state.data_source.localCsvFile.dto.ProgressionStateCsvDto
import net.thechance.data.authentication.UserSession
import data.projects.data_source.ProjectsDataSource
import data.projects.data_source.remote.mongo.MongoProjectDataSource
import data.projects.data_source.localcsvfile.dto.ProjectCsvDto
import net.thechance.data.tasks.data_source.remote.mongo.MongoTaskDataSource
import net.thechance.data.tasks.data_source.localCsvFile.dto.TaskCsvDto
import net.thechance.data.user.data_source.remote.mongo.UserMongoDataSource
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule = module {

	fun <T : CsvSerializable> Module.csvFileSetup(
		filePath: String,
		namedPrefix: String,
		factory: (List<String>) -> T
	) {
		single(named("${namedPrefix}CsvFile")) { File(filePath) }
		single(named("${namedPrefix}FileHandler")) {
			CsvFileHandler(get(named("${namedPrefix}CsvFile")))
		}
		single(named("${namedPrefix}FileParser")) {
			CsvFileParser(factory = factory)
		}
	}

	csvFileSetup(
		filePath = "data_files/tasks.csv",
		namedPrefix = "tasks",
		factory = TaskCsvDto.Companion::fromCsv
	)
	csvFileSetup(
		filePath = "data_files/users.csv",
		namedPrefix = "users",
		factory = UserCsvDto.Companion::fromCsv
	)
	csvFileSetup(
		filePath = "data_files/audit_log.csv",
		namedPrefix = "auditLog",
		factory = AuditLogCsvDto.Companion::fromCsv
	)
	csvFileSetup(
		filePath = "data_files/projects.csv",
		namedPrefix = "projects",
		factory = ProjectCsvDto.Companion::fromCsv
	)
	csvFileSetup(
		filePath = "data_files/states.csv",
		namedPrefix = "progressionStates",
		factory = ProgressionStateCsvDto.Companion::fromCsv
	)


	single { UserSession() }
	single<UsersDataSource> {
		UserMongoDataSource(get(named("usersCollection")))
	}

	single<TasksDataSource> {
		MongoTaskDataSource(
			taskCollection = get(named("tasksCollection"))
		)
	}

	single<ProgressionStateDataSource> {
		ProgressionStateDatabaseDataSource(
			progressionStatesCollection = get(named("progressionStatesCollection"))
		)
	}

	single<AuditLogDataSource> {
		MongoAuditLogDataSource(get(named("auditLogsCollection")))
	}

	single<ProjectsDataSource> {
		MongoProjectDataSource(
			projectsCollection = get(named("projectsCollection")),
			tasksDataSource = get<TasksDataSource>(),
			statesDataSource = get<ProgressionStateDataSource>()
		)
	}




}

