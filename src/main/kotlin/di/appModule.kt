package di

import data.user.data_source.UsersFileDataSource
import logic.entities.AuditLog
import logic.entities.Task
import logic.entities.User
import data.aduit_log_csvfile.data_source.AuditLogDataSource
import data.aduit_log_csvfile.data_source.AuditLogFileDataSource
import data.authentication.utils.PasswordHashing
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.projects.ProjectsDataSource
import data.projects.ProjectsFileDataSource
import data.states.data_source.StatesDataSource
import data.states.data_source.StatesFileDataSource
import data.tasks.data_source.TasksDataSource
import data.tasks.data_source.TasksFileDataSource
import data.user.data_source.UsersDataSource
import logic.entities.*
import logic.use_cases.state.stateValidations.StateValidationImp
import logic.use_cases.state.stateValidations.StateValidator
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import org.koin.dsl.module

val appModule = module {

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }

    single { PasswordHashing() }

        single(named("stateCsvFile")) { File("src/main/resources/states.csv") }

        single(named("statesFileHandler")) { CsvFileHandler(get(named("stateCsvFile"))) }

        single(named("statesFileParser")) { CsvFileParser(factory = ProgressionState.Companion::fromCsv) }


        single<StatesDataSource> {
            StatesFileDataSource(
                statesFileHandler = get(named("statesFileHandler")),
                csvFileParser = get(named("statesFileParser"))
            )
        }
    single<StateValidator> { StateValidationImp(get(), get()) }

}
