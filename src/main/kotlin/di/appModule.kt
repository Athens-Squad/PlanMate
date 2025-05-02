package net.thechance.di

import logic.entities.AuditLog
import logic.entities.Task
import logic.entities.User
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogDataSource
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogFileDataSource
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.states.data_source.StatesDataSource
import net.thechance.data.states.data_source.StatesFileDataSource
import net.thechance.data.tasks.data_source.TasksDataSource
import net.thechance.data.tasks.data_source.TasksFileDataSource
import net.thechance.data.user.data_source.UsersDataSource
import net.thechance.data.user.data_source.UsersFileDataSource
import net.thechance.logic.entities.State
import net.thechance.logic.use_cases.state.stateValidations.StateValidationImp
import net.thechance.logic.use_cases.state.stateValidations.StateValidator
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

    single<TaskValidator> {TaskValidatorImpl(get(), get(), get()) }

    single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }

    single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }

    single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLog.Companion::fromCsv) }


    single<AuditLogDataSource> {
        AuditLogFileDataSource(
            auditLogFileHandler = get(named("AuditLogFileHandler")),
            csvFileParser = get(named("AuditLogFileParser"))
        )
    }

    single(named("usersCsvFile")) { File("data_files/users.csv") }

    single(named("usersFileHandler")) { CsvFileHandler(get(named("usersCsvFile"))) }

    single(named("usersFileParser")) { CsvFileParser(factory = User.Companion::fromCsv) }

    single<UsersDataSource> {
        UsersFileDataSource(
            userFileHandler = get(named("usersFileHandler")),
            csvFileParser = get(named("usersFileParser"))
        )
    }

        single(named("stateCsvFile")) { File("src/main/resources/states.csv") }

        single(named("statesFileHandler")) { CsvFileHandler(get(named("stateCsvFile"))) }

        single(named("statesFileParser")) { CsvFileParser(factory = State.Companion::fromCsv) }


        single<StatesDataSource> {
            StatesFileDataSource(
                statesFileHandler = get(named("statesFileHandler")),
                csvFileParser = get(named("statesFileParser"))
            )
        }
    single<StateValidator> {StateValidationImp(get(), get()) }

}

