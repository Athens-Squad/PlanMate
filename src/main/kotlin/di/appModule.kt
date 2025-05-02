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





    single { PasswordHashing() }
}
