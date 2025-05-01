package di

import data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import data.authentication.repository.AuthRepositoryImpl
import data.states.StatesRepositoryImpl
import data.tasks.repository.TasksRepositoryImpl
import data.user.repository.UserRepositoryImpl
import logic.repositories.*
import data.projects.ProjectsRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<AuditRepository> { AuditLogRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<StatesRepository> { StatesRepositoryImpl() }

    single<TasksRepository> { TasksRepositoryImpl(get()) }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }
    single<AuditRepository> {AuditLogRepositoryImpl(get()) }
}