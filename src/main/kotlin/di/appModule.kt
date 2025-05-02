package di

import data.authentication.utils.PasswordHashing
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.projects.ProjectsDataSource
import data.projects.ProjectsFileDataSource
import data.tasks.data_source.TasksDataSource
import data.tasks.data_source.TasksFileDataSource
import data.user.data_source.UsersDataSource
import logic.entities.Project
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import org.koin.dsl.module

val appModule = module {

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

    single(named("projectsCsvFile")) { File("data_files/projects.csv") }

    single(named("projectsFileHandler")) { CsvFileHandler(get(named("projectsCsvFile"))) }

    single(named("projectsFileParser")) { CsvFileParser(factory = Project.Companion::fromCsv) }


    single<ProjectsDataSource> {
        ProjectsFileDataSource(
            projectsFileHandler = get(named("projectsFileHandler")),
            csvFileParser = get(named("projectsFileParser")),
            tasksFileDataSource = get()
        )
    }


    single { PasswordHashing() }
}
