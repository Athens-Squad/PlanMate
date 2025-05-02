package di

import logic.repositories.*
import data.user.repository.UserRepositoryImpl
import data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import data.authentication.repository.AuthRepositoryImpl
import data.projects.ProjectsRepositoryImpl
import data.tasks.repository.TasksRepositoryImpl
import logic.repositories.AuthenticationRepository
import data.states.repository.StatesRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<AuditRepository> { AuditLogRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl() }
    single<StatesRepository> { StatesRepositoryImpl(get()) }

    single<TasksRepository> { TasksRepositoryImpl(get()) }


    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }

}